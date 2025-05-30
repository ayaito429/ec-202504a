package com.example.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.common.CustomUserDetails;
import com.example.domain.Order;
import com.example.form.OrderForm;
import com.example.service.OrderService;
import com.example.service.UserService;

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
	private UserService userService;

	@ModelAttribute
	public OrderForm setOrderForm() {
		return new OrderForm();
	}

	@RequestMapping("/toOrder")
	public String toOrder() {
		return "order/order_confirm";
	}

	@RequestMapping("/orderCo")
	public String orderCo() {
		return "order/order_confirm";
	}

	/**
	 * 注文完了画面に遷移
	 * 
	 * @param form
	 * @return 完了画面
	 */
	// @RequestMapping("/order")
	// public String orderCompletion(@Validated OrderForm form, BindingResult
	// result, Model model,
	// @AuthenticationPrincipal CustomUserDetails customUserDetails) {
	// // 配達日チェック
	// Date today = new Date();
	// Calendar yesterday = Calendar.getInstance();
	// yesterday.setTime(today);
	// yesterday.add(Calendar.DAY_OF_MONTH, -1);
	// Date minDate = yesterday.getTime();

	// if (result.hasErrors() || form.getOrderDate() == null) {
	// model.addAttribute("errorDeliveryDate", "配達日を入力してください");
	// return "order/order_confirm";
	// }
	// if (minDate.after(form.getOrderDate())) {
	// model.addAttribute("errorDeliveryDate", "配達日が過去の日付になっています");
	// return "order/order_confirm";
	// }

	// // 3時間後の時間チェック
	// Calendar timePlusThree = Calendar.getInstance();
	// timePlusThree.setTime(today);
	// timePlusThree.add(Calendar.HOUR_OF_DAY, 3);
	// Date minDeliveryTime = timePlusThree.getTime();

	// SimpleDateFormat yearFmt = new SimpleDateFormat("yyyy");
	// SimpleDateFormat monthFmt = new SimpleDateFormat("MM");
	// SimpleDateFormat dayFmt = new SimpleDateFormat("dd");

	// int deliveryYear = Integer.parseInt(yearFmt.format(form.getOrderDate()));
	// int deliveryMonth = Integer.parseInt(monthFmt.format(form.getOrderDate()));
	// int deliveryDay = Integer.parseInt(dayFmt.format(form.getOrderDate()));

	// @SuppressWarnings("deprecation")
	// Date deliveryTime = new Date(deliveryYear - 1900, deliveryMonth - 1,
	// deliveryDay, form.getIntegerDeliveryTime(),
	// 0, 0);

	// if (minDeliveryTime.after(deliveryTime)) {
	// model.addAttribute("errorDeliveryDate", "今から3時間後の日時をご入力ください");
	// return "order/order_confirm";
	// }

	// // 注文情報を作成
	// Order order = new Order();
	// BeanUtils.copyProperties(form, order);
	// order.setDestinationZipcode(form.getDestinationZipcode().replace("-", ""));
	// order.setDeliveryTime(form.getTimestamp());
	// order.setUserId(customUserDetails.getUserId());

	// orderService.order(order);

	// // 完了メール送信
	// orderService.sendMail(customUserDetails.getEmail());

	// return "redirect:/orderCompletion";
	// }

	@RequestMapping("/orderCompletion")
	public String orderCompletionPage() {
		return "order/order_finished";
	}

	/**
	 * 注文履歴のページを表示
	 * 
	 * @param model リクエストスコープ
	 * @return 注文履歴
	 */
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
}
