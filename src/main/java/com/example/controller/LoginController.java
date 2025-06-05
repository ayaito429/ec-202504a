package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ログイン画面に遷移するためのコントローラクラス
 * 
 * @author shirota sho
 */
@Controller
public class LoginController {

	/**
	 * ログイン画面の表示
	 * 
	 * @param error  エラーステータス
	 * @param logout ログアウトステータス
	 * @param model  リクエストスコープの格納用
	 * @return ログイン画面
	 */
	@GetMapping("/toLogin")
	public String loginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			Model model) {

		if (error != null) {
			model.addAttribute("loginError", "メールアドレス、またはパスワードが間違っています");
		}

		if (logout != null) {
			model.addAttribute("logoutMessage", "ログアウトしました");
		}

		return "login/login";
	}

	@GetMapping("/error500")
public String throwError() {
    String str = null;
    str.length(); // ← NullPointerException 発生
    return "some-view";
}
}
