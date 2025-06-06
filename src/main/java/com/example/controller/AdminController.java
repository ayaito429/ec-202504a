package com.example.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.domain.User;
import com.example.form.AdminOrderDetailForm;
import com.example.form.AdminOrderListForm;
import com.example.service.OrderService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private OrderService orderService;

	// 1ページに表示する注文数は30件
	private static final int VIEW_SIZE = 30;

	@RequestMapping("")
	public String index() {
		return "admin/order_list";
	}

	@RequestMapping("/list")
	public String getOrders(AdminOrderListForm form, Model model) {

		// 検索件数
		Integer result = 0;

		// ページング機能追加
		if (form.getPage() == null) {
			// ページ数の指定が無い場合は1ページ目を表示させる
			form.setPage(1);
		}

		List<Order> orderList = new ArrayList<>();
		if (form.getSearchField().equals("orderDate") || form.getSearchField().equals("deliveryTime")
				|| form.getSearchField().equals("completionTime")) {
			orderList = orderService.searchOrders(form.getSearchField(), form.getSearchValue1(),
					form.getSearchValue2());
		} else if (form.getSearchValue1() != null) {
			orderList = orderService.searchOrders(form.getSearchField(), form.getSearchValue1());
		} else {
			orderList = orderService.searchOrders(form.getSearchField(), form.getSearchValue2());
		}

		// 該当する注文が無かった場合
		if (orderList.isEmpty()) {
			result = 0;
		} else {
			for (int i = 0; i < orderList.size(); i++) {
				User user = orderList.get(i).getUser();
				orderList.get(i).setUser(user);
			}
			result = orderList.size();
		}

		// 表示させたいページ数、ページサイズ、注文リストを渡し１ページに表示させる注文リストを絞り込み
		Page<Order> orderPage = orderService.showListPaging(form.getPage(), VIEW_SIZE, orderList);
		model.addAttribute("orderPage", orderPage);

		// ページングのリンクに使うページ数をスコープに格納 (例)28件あり1ページにつき10件表示させる場合→1,2,3がpageNumbersに入る
		List<Integer> pageNumbers = calcPageNumbers(model, orderPage);
		model.addAttribute("pageNumbers", pageNumbers);

		model.addAttribute("result", result);
		model.addAttribute("orderList", orderList);
		model.addAttribute("searchField", form.getSearchField());
		model.addAttribute("searchValue1", form.getSearchValue1());
		model.addAttribute("searchValue2", form.getSearchValue2());
		return "admin/order_list";
	}

	/**
	 * ページングのリンクに使うページ数をスコープに格納 (例)28件あり1ページにつき10件表示させる場合→1,2,3がpageNumbersに入る
	 * 
	 * @param model     モデル
	 * @param orderPage ページング情報
	 */
	private List<Integer> calcPageNumbers(Model model, Page<Order> orderPage) {
		int totalPages = orderPage.getTotalPages();
		System.out.println(totalPages);
		List<Integer> pageNumbers = null;
		if (totalPages > 0) {
			pageNumbers = new ArrayList<Integer>();
			for (int i = 1; i <= totalPages; i++) {
				pageNumbers.add(i);
			}
		}
		System.out.println(orderPage);
		model.addAttribute("orderPage", orderPage);
		return pageNumbers;
	}

	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable("id") Integer id, Model model) {
		List<Order> orderList = orderService.orderLoad(id);
		AdminOrderDetailForm form = new AdminOrderDetailForm();
		for (int i = 0; i < orderList.size(); i++) {
			form.setStatus(orderList.get(i).getStatus());
			form.setId(id);
		}
		model.addAttribute("adminOrderDetailForm", form);

		System.out.println(orderList);
		model.addAttribute("orderList", orderList);
		return "admin/order_detail";
	}

	@RequestMapping("/detail/update")
	public String update(AdminOrderDetailForm form, Model model) {
		boolean errorFlg = false;

		Timestamp completionTime = form.getTimestamp();

		if (completionTime == null) {
			errorFlg = true;
			model.addAttribute("errorFlg", errorFlg);
			return "admin/order_detail";
		} else {
			orderService.updateCompletionTime(completionTime, form.getId(), form.getStatus());
		}
		return "admin/order_list";
	}

}
