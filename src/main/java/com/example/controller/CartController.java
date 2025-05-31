package com.example.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.common.CustomUserDetails;
import com.example.domain.CartItem;
import com.example.domain.CartOrder;
import com.example.form.ItemCartInForm;
import com.example.service.CartService;
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
	private CartService service;

	@Autowired
	private HttpSession session;

	public ItemCartInForm setupForm() {
		return new ItemCartInForm();
	}

	// Cartに商品を追加
	@RequestMapping("/inCart")
	public String inCart(ItemCartInForm form) {

		// CartItem cartItem = new CartItem();
		// BeanUtils.copyProperties(form, cartItem);
		// cartItem.setItemId(form.getId());

		// // cartItemにitemの金額を設置
		// cartItem.setItemPrice(service.getPriceSize(form));

		// // トッピングをcartItemに代入
		// @SuppressWarnings("unchecked")
		// List<Topping> toppingList = (List<Topping>)
		// application.getAttribute("toppingList");
		// List<Topping> toppings = service.getToppingIndex(toppingList,
		// form.getToppingIndex());
		// cartItem.setToppingList(toppings);

		// // 小計を代入
		// // Integer subPrices = service.calcSubTotal(cartItem);
		// // cartItem.setSubTotal(subPrices);

		// // カート内の商品をリストに格納
		// // 初めてセッションスコープに格納する際はLinkedListを入れる
		// if (session.getAttribute("cartItemList") == null) {
		// List<CartItem> cartItemList = new LinkedList<>();
		// session.setAttribute("cartItemList", cartItemList);
		// }

		// @SuppressWarnings("unchecked")
		// List<CartItem> cartItemList = (List<CartItem>)
		// session.getAttribute("cartItemList");
		// cartItemList.add(cartItem);
		// session.setAttribute("cartItemList", cartItemList);

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

		if (customUserDetails != null) {
			Integer userId = customUserDetails.getUserId();
			CartOrder cartOrder = service.findForCartByUserId(userId);
			model.addAttribute("cartOrder", cartOrder);
			if (cartOrder == null) {
				model.addAttribute("cartNothing", "カートの中身はございません");
			}
		} else {
			CartOrder cartOrder = (CartOrder) session.getAttribute("cartOrder");
			if (cartOrder != null) {
				model.addAttribute("cartOrder", cartOrder);
			} else {
				model.addAttribute("cartNothing", "カートの中身はございません");
			}
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
			service.deleteForCartItem(Integer.parseInt(cartItemId));
		}
		try {
			@SuppressWarnings("unchecked")
			List<CartItem> cartItemList = (List<CartItem>) session.getAttribute("cartItemList");
			if (cartItemList != null) {
				int idx = Integer.parseInt(index);
				if (idx >= 0 && idx < cartItemList.size()) {
					cartItemList.remove(idx);
				}
			}
		} catch (NumberFormatException e) {
			System.out.println("インデックスが不正です: " + index);
		}
		return "redirect:/showCart";
	}

}
