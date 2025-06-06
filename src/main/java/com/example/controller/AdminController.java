package com.example.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.Order;
import com.example.domain.User;
import com.example.form.AdminOrderDetailForm;
import com.example.form.AdminOrderListForm;
import com.example.service.OrderService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@ModelAttribute
	public AdminOrderDetailForm setUpForm() {
		return new AdminOrderDetailForm();
	}

	@Autowired
	private OrderService orderService;

	// 1ページに表示する注文数は30件
	private static final int VIEW_SIZE = 30;

	/**
	 * 初期表示画面
	 * 
	 * @return 注文一覧画面
	 * 
	 */
	@RequestMapping("")
	public String index() {
		return "admin/order_list";
	}

	/**
	 * 検索結果を表示
	 * 
	 * @param form  検索値入力フォーム
	 * @param model 情報を格納するオブジェクト
	 * @return 注文一覧画面
	 */
	@RequestMapping("/list")
	public String getOrders(AdminOrderListForm form, Model model) {

		boolean emptyFlg = false;

		// 検索件数
		Integer result = 0;

		// ページング機能追加
		if (form.getPage() == null) {
			// ページ数の指定が無い場合は1ページ目を表示させる
			form.setPage(1);
		}

		List<Order> orderList = new ArrayList<>();
		// 日付検索をする場合
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
			emptyFlg = true;
		} else {
			for (int i = 0; i < orderList.size(); i++) {
				User user = orderList.get(i).getUser();
				orderList.get(i).setUser(user);
			}
			result = orderList.size();
		}

		// 表示させたいページ数、ページサイズ、注文リストを渡し１ページに表示させる注文情報を絞り込み
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
		model.addAttribute("emptyFlg", emptyFlg);

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

	/**
	 * 注文詳細画面を表示
	 * 
	 * @param id    注文ID
	 * @param model 情報を格納するオブジェクト
	 * @return 注文詳細画面
	 */
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable("id") Integer id, Model model, AdminOrderDetailForm redirectForm) {

		List<Order> orderList = orderService.orderLoad(id);

		if (redirectForm.getStatus() != null) {
			redirectForm.setId(id);
			model.addAttribute("adminOrderDetailForm", redirectForm);
		} else {
			AdminOrderDetailForm form = new AdminOrderDetailForm();
			System.out.println(form);

			for (int i = 0; i < orderList.size(); i++) {
				form.setStatus(orderList.get(i).getStatus());
				form.setId(id);
			}
			model.addAttribute("adminOrderDetailForm", form);
		}

		model.addAttribute("orderList", orderList);

		return "admin/order_detail";
	}

	/**
	 * 注文情報を更新
	 * 
	 * @param form  更新情報入力フォーム
	 * @param model 情報を格納するオブジェクト
	 * @return 注文一覧画面
	 */
	@RequestMapping("/detail/update")
	public String update(@ModelAttribute AdminOrderDetailForm form, Model model,
			RedirectAttributes redirectAttributes) {

		boolean errorFlg = false;
		boolean completeFlg = false;
		Timestamp completionTime = form.getTimestamp();

		if (completionTime == null && form.getStatus() == 4) {

			errorFlg = true;

			redirectAttributes.addFlashAttribute("errorFlg", errorFlg);
			redirectAttributes.addFlashAttribute("adminOrderDetailForm", form);

			return "redirect:/admin/detail/" + form.getId();
		} else {
			
			orderService.updateCompletionTime(completionTime, form.getId(), form.getStatus());
			completeFlg = true;
			model.addAttribute("completeFlg", completeFlg);
		}
		return "admin/order_list";
	}

}
