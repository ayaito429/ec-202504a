package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.common.CustomUserDetails;
import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.form.ItemCartInForm;
import com.example.service.ItemService;
import com.example.service.OrderService;

import jakarta.servlet.http.HttpSession;

/**
 * @author satakemisako
 *         カートに商品を追加する
 *
 */
@Controller
@RequestMapping("")
public class CartController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private HttpSession session;

	@Autowired
	private MessageSource messageSource;

	public ItemCartInForm setupForm() {
		return new ItemCartInForm();
	}

	private Order getCartOrder(CustomUserDetails customUserDetails) {
		if (customUserDetails != null) {
			List<Order> cartOrders = orderService.findByStatus(customUserDetails.getUserId(), 0);
			if (cartOrders == null || cartOrders.isEmpty()) {
				return null;
			}
			return cartOrders.get(0);
		} else {
			return (Order) session.getAttribute("cartOrder");
		}
	}

	private OrderItem createOrderItem(ItemCartInForm form, Item item, List<OrderTopping> orderToppings) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItemId(item.getId());
		orderItem.setQuantity(form.getQuantity());
		orderItem.setSize(form.getSize());
		orderItem.setOrderTopping(orderToppings);
		orderItem.setItem(item);
		return orderItem;
	}

	private boolean toppingsEqual(List<OrderTopping> list1, List<OrderTopping> list2) {
		if (list1.size() != list2.size()) {
			return false;
		}
		Set<Integer> toppingIds1 = list1.stream()
				.map(t -> t.getTopping().getId())
				.collect(Collectors.toSet());

		Set<Integer> toppingIds2 = list2.stream()
				.map(t -> t.getTopping().getId())
				.collect(Collectors.toSet());

		return toppingIds1.equals(toppingIds2);
	}

	// Cartに商品を追加
	@RequestMapping("/inCart")
	public String inCart(ItemCartInForm form, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Item item = itemService.showItemDetail(form.getId());
		List<OrderTopping> orderToppings = new ArrayList<>();
		if (form.getToppingIndex() != null) {
			for (String toppingId : form.getToppingIndex()) {
				Topping topping = itemService.findToppingById(Integer.parseInt(toppingId));
				OrderTopping orderTopping = new OrderTopping();
				orderTopping.setToppingId(topping.getId());
				Integer price = form.getSize().equals("M") ? topping.getPriceM() : topping.getPriceL();
				orderTopping.setPrice(price);
				orderTopping.setTopping(topping);
				orderToppings.add(orderTopping);
			}
		}

		OrderItem orderItem = createOrderItem(form, item, orderToppings);

		Integer itemPrice = orderItem.getSize().equals("M") ? orderItem.getItem().getPriceM()
				: orderItem.getItem().getPriceL();
		orderItem.setItemPrice(itemPrice);
		Integer addedTotalPrice = itemPrice * orderItem.getQuantity();

		for (OrderTopping orderTopping : orderItem.getOrderTopping()) {
			Integer toppingPrice = orderItem.getSize().equals("M") ? orderTopping.getTopping().getPriceM()
					: orderTopping.getTopping().getPriceL();
			orderTopping.setPrice(toppingPrice);
			addedTotalPrice += toppingPrice * orderItem.getQuantity();
		}

		Order cartOrder = getCartOrder(customUserDetails);

		if (customUserDetails != null) {
			if (cartOrder == null) {
				Order newOrder = new Order();
				newOrder.setUserId(customUserDetails.getUserId());
				newOrder.setStatus(0);
				newOrder.setTotalPrice(addedTotalPrice);
				Integer orderId = orderService.insert(newOrder);
				orderItem.setOrderId(orderId);
				orderService.insertOrderItem(orderItem);
			} else {
				orderItem.setOrderId(cartOrder.getId());
				List<OrderItem> cartOrderItems = cartOrder.getOrderItemList();
				boolean isMerged = false;

				for (OrderItem existingItem : cartOrderItems) {
					if (existingItem.getItemId().equals(orderItem.getItemId())
							&& existingItem.getSize().equals(orderItem.getSize())
							&& toppingsEqual(existingItem.getOrderTopping(), orderItem.getOrderTopping())) {
						orderService.addQuantity(existingItem.getId(),
								existingItem.getQuantity() + orderItem.getQuantity());
						existingItem.setQuantity(existingItem.getQuantity() + orderItem.getQuantity());
						isMerged = true;
						break;
					}
				}

				if (!isMerged) {
					orderService.insertOrderItem(orderItem);
				}

				cartOrder.setTotalPrice(cartOrder.getTotalPrice() + addedTotalPrice);
				orderService.update(cartOrder);
			}
		} else {
			if (cartOrder == null) {
				cartOrder = new Order();
				cartOrder.setStatus(0);
				cartOrder.setTotalPrice(addedTotalPrice);
				List<OrderItem> orderItemList = new ArrayList<>();
				orderItemList.add(orderItem);
				cartOrder.setOrderItemList(orderItemList);
				session.setAttribute("cartOrder", cartOrder);
			} else {
				List<OrderItem> cartOrderItems = cartOrder.getOrderItemList();
				boolean isMerged = false;

				for (OrderItem existingItem : cartOrderItems) {
					if (existingItem.getItemId().equals(orderItem.getItemId())
							&& existingItem.getSize().equals(orderItem.getSize())
							&& toppingsEqual(existingItem.getOrderTopping(), orderItem.getOrderTopping())) {
						existingItem.setQuantity(existingItem.getQuantity() + orderItem.getQuantity());
						isMerged = true;
						break;
					}
				}

				if (!isMerged) {
					cartOrder.getOrderItemList().add(orderItem);
				}

				cartOrder.setTotalPrice(cartOrder.getTotalPrice() + addedTotalPrice);
			}
		}
		return "redirect:/showCart";
	}

	/**
	 * Cartの中身を表示するメソッド
	 * 
	 * @param model
	 * @param customUserDetails
	 * @return
	 */
	@RequestMapping("/showCart")
	public String showCart(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Order cartOrder = getCartOrder(customUserDetails);

		if (cartOrder != null && cartOrder.getOrderItemList() != null && !cartOrder.getOrderItemList().isEmpty()) {
			Integer totalPrice = 0;
			for (OrderItem orderItem : cartOrder.getOrderItemList()) {
				Integer itemPrice = orderItem.getSize().equals("M") ? orderItem.getItem().getPriceM()
						: orderItem.getItem().getPriceL();
				orderItem.setItemPrice(itemPrice);
				totalPrice += itemPrice * orderItem.getQuantity();
				for (OrderTopping orderTopping : orderItem.getOrderTopping()) {
					Integer toppingPrice = orderItem.getSize().equals("M") ? orderTopping.getTopping().getPriceM()
							: orderTopping.getTopping().getPriceL();
					orderTopping.setPrice(toppingPrice);
					totalPrice += toppingPrice * orderItem.getQuantity();
				}
			}
			cartOrder.setTotalPrice(totalPrice);
			model.addAttribute("cartOrder", cartOrder);
		} else {
			model.addAttribute("cartNothing",
					messageSource.getMessage("cartNothing", null, "カートに商品がありません", Locale.JAPAN));
		}

		return "cart/cart_list";
	}

	/**
	 * カート内の商品を削除するメソッド
	 * 
	 * @param cartItemId
	 * @param index
	 * @param customUserDetails
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(String cartItemId, String index,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		if (customUserDetails != null) {
			orderService.deleteForCartItem(Integer.parseInt(cartItemId));
		} else {
			Order cartOrder = (Order) session.getAttribute("cartOrder");
			if (cartOrder != null) {
				try {
					int idx = Integer.parseInt(index);
					if (idx >= 0 && idx < cartOrder.getOrderItemList().size()) {
						cartOrder.getOrderItemList().remove(idx);
					}
				} catch (NumberFormatException e) {
					System.out.println("インデックスが不正です: " + index);
				}
			}
		}
		return "redirect:/showCart";
	}

}
