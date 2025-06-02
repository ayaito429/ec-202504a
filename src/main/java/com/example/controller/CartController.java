package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		Integer totalPrice = 0;
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
		Order cartOrder = getCartOrder(customUserDetails);

		if (customUserDetails != null) {
			if (cartOrder == null) {
				Order newOrder = new Order();
				newOrder.setUserId(customUserDetails.getUserId());
				newOrder.setStatus(0);
				newOrder.setTotalPrice(totalPrice);
				Integer orderId = orderService.insert(newOrder);
				orderItem.setOrderId(orderId);
				orderService.insertOrderItem(orderItem);
			} else {
				orderItem.setOrderId(cartOrder.getId());
				orderService.insertOrderItem(orderItem);
			}
		} else {
			if (cartOrder == null) {
				cartOrder = new Order();
				cartOrder.setStatus(0);
				cartOrder.setTotalPrice(totalPrice);
				session.setAttribute("cartOrder", cartOrder);
			}
			cartOrder.getOrderItemList().add(orderItem);
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
			model.addAttribute("cartNothing", "カートの中身はございません");
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
