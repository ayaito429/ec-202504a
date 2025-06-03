package com.example.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.common.CustomUserDetails;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.form.OrderForm;
import com.example.service.ItemService;
import com.example.service.OrderService;

/**
 * 注文確認画面に遷移するためのコントローラー
 * 
 * @author MatsunagaDai,MiyazawaNami
 *
 */
@Controller
@RequestMapping("")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ItemService itemService;

	@ModelAttribute
	public OrderForm setOrderForm() {
		return new OrderForm();
	}

	@RequestMapping("/toOrder")
	public String toOrder(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails,
			RedirectAttributes redirectAttributes) {

		List<Order> cartOrders = orderService.findByStatus(customUserDetails.getUserId(), 0);

		if (cartOrders.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "カートの中身はございません");
			return "redirect:/showCart";
		}

		Order cartOrder = cartOrders.get(0);
		Map<Integer, String> errorMessages = validateStock(cartOrder);

		if (!errorMessages.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
			redirectAttributes.addFlashAttribute("cartOrder", cartOrder);
			return "redirect:/showCart";
		}

		model.addAttribute("cartOrder", cartOrder);

		OrderForm orderForm = new OrderForm();
		orderForm.setDestinationName(cartOrder.getUser().getName());
		orderForm.setDestinationEmail(cartOrder.getUser().getEmail());
		orderForm.setDestinationZipcode(cartOrder.getUser().getZipcode());
		orderForm.setDestinationAddress(cartOrder.getUser().getAddress());
		orderForm.setDestinationTel(cartOrder.getUser().getTelephone());
		model.addAttribute("orderForm", orderForm);

		return "order/order_confirm";
	}

	@RequestMapping("/order")
	public String orderCompletion(@Validated OrderForm form, BindingResult result, Model model,
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			RedirectAttributes redirectAttributes) {

		List<Order> cartOrders = orderService.findByStatus(customUserDetails.getUserId(), 0);
		if (cartOrders.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "カートが空です");
			return "redirect:/showCart";
		}

		Order cartOrder = cartOrders.get(0);
		Map<Integer, String> errorMessages = validateStock(cartOrder);

		if (!errorMessages.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
			redirectAttributes.addFlashAttribute("cartOrder", cartOrder);
			return "redirect:/showCart";
		}

		if (result.hasErrors()) {
			model.addAttribute("cartOrder", cartOrder);
			model.addAttribute("orderForm", form);
			return "order/order_confirm";
		}

		Timestamp deliveryTime = form.getTimestamp();
		Timestamp minDeliveryTime = new Timestamp(System.currentTimeMillis() + (3 * 60 * 60 * 1000));
		if (minDeliveryTime.after(deliveryTime)) {
			model.addAttribute("errorDeliveryDate", "今から3時間後の日時をご入力ください。");
			model.addAttribute("cartOrder", cartOrder);
			model.addAttribute("orderForm", form);
			return "order/order_confirm";
		}

		cartOrder.setOrderDate(Date.valueOf(LocalDate.now()));
		cartOrder.setDestinationName(form.getDestinationName());
		cartOrder.setDestinationEmail(form.getDestinationEmail());
		cartOrder.setDestinationZipcode(form.getDestinationZipcode());
		cartOrder.setDestinationAddress(form.getDestinationAddress());
		cartOrder.setDestinationTel(form.getDestinationTel());
		cartOrder.setDeliveryTime(deliveryTime);
		cartOrder.setPaymentMethod(form.getPaymentMethod());
		cartOrder.setStatus(2);

		orderService.update(cartOrder);

		for (OrderItem orderItem : cartOrder.getOrderItemList()) {
			orderService.insertItemPrice(orderItem);
			itemService.updateStock(orderItem);
			for (OrderTopping orderTopping : orderItem.getOrderTopping()) {
				orderService.insertToppingPrice(orderTopping);
			}
		}

		orderService.sendMail(customUserDetails.getEmail());
		return "redirect:/orderCompletion";
	}

	private Map<Integer, String> validateStock(Order cartOrder) {
		Map<Integer, String> errorMessages = new HashMap<>();
		Integer totalPrice = 0;

		List<OrderItem> orderItems = cartOrder.getOrderItemList();

		Map<Integer, Integer> itemQuantityMap = new HashMap<>();
		for (OrderItem orderItem : orderItems) {
			Integer itemId = orderItem.getItemId();
			Integer quantity = orderItem.getQuantity();
			itemQuantityMap.put(itemId, itemQuantityMap.getOrDefault(itemId, 0) + quantity);
		}

		for (Map.Entry<Integer, Integer> entry : itemQuantityMap.entrySet()) {
			Integer itemId = entry.getKey();
			Integer totalQuantity = entry.getValue();

			Integer stock = itemService.findForStockById(itemId);

			String itemName = orderItems.stream()
					.filter(oi -> oi.getItemId().equals(itemId))
					.findFirst()
					.map(oi -> oi.getItem().getName())
					.orElse("商品ID " + itemId);

			if (stock == 0) {
				errorMessages.put(itemId, "「" + itemName + "」は在庫切れです。");
			} else if (stock < totalQuantity) {
				errorMessages.put(itemId, "「" + itemName + "」は在庫が足りません（在庫: " + stock + "）。");
			}
		}

		for (OrderItem orderItem : orderItems) {
			Integer itemPrice = orderItem.getSize().equals("M")
					? orderItem.getItem().getPriceM()
					: orderItem.getItem().getPriceL();
			orderItem.setItemPrice(itemPrice);
			totalPrice += itemPrice * orderItem.getQuantity();

			for (OrderTopping orderTopping : orderItem.getOrderTopping()) {
				Integer toppingPrice = orderItem.getSize().equals("M")
						? orderTopping.getTopping().getPriceM()
						: orderTopping.getTopping().getPriceL();
				orderTopping.setPrice(toppingPrice);
				totalPrice += toppingPrice * orderItem.getQuantity();
			}
		}

		cartOrder.setTotalPrice(totalPrice);
		return errorMessages;
	}

	@RequestMapping("/orderHistory")
	public String orderHistory(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Integer userId = customUserDetails.getUserId();
		List<Order> orderList = orderService.findByOrder(userId);
		if (orderList == null || orderList.isEmpty()) {
			model.addAttribute("orderNothing", "注文履歴がありません");
		} else {
			model.addAttribute("orderList", orderList);
		}
		return "order/order_history";
	}

	@RequestMapping("/orderdetail")
	public String orderDetail(Integer id, Model model) {
		List<Order> orderList = orderService.orderLoad(id);
		model.addAttribute("orderList", orderList);
		return "/order/order_detail";
	}

	@RequestMapping("/orderCompletion")
	public String orderCompletionPage() {
		return "order/order_finished";
	}
}
