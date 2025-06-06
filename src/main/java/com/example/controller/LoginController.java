package com.example.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ログイン画面に遷移するためのコントローラクラス
 * 
 * @author shirota sho
 */
@Controller
public class LoginController {

	@Autowired
	private MessageSource messageSource;

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
			model.addAttribute("loginError", messageSource.getMessage("loginError", null, "ログインエラー", Locale.JAPAN));
		}

		if (logout != null) {
			model.addAttribute("logoutMessage", messageSource.getMessage("logout", null, "ログアウトしました", Locale.JAPAN));
		}

		return "login/login";
	}

	/**
	 * ルートのパスにリクエストを送信した際にリダイレクト処理
	 * @return リダイレクト先
	 */
	@RequestMapping("")
    public String redirectToList() {
        return "redirect:/showList";
    }

	@GetMapping("/error500")
public String throwError() {
    String str = null;
    str.length(); // ← NullPointerException 発生
    return "some-view";
}
}
