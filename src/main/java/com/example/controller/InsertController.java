package com.example.controller;



import java.util.Locale;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.User;
import com.example.form.InsertForm;
import com.example.service.UserService;

import jakarta.servlet.http.HttpSession;


/**
 * ユーザー情報を登録するためのコントローラー
 * @author matsunagadai
 *
 */
@Controller
@RequestMapping("/insert")
public class InsertController {
	

	@ModelAttribute
	public InsertForm setUpForm() {
		return new InsertForm();
	}
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * ユーザー登録画面に遷移
	 * @return
	 */
	@RequestMapping("")
	public String toInsert() {
		String email = (String) session.getAttribute("email");
		if(email == null) {
			return "redirect:/mailInsert";
		}
		return "register_user";
	}

	/**
	 * フォームから受け取った情報をもとにユーザー登録を行う。
	 * 登録完了後ログイン画面に遷移。
	 * @param form
	 * @return
	 */
	@RequestMapping("/insertUser")
	public String insert(@Validated InsertForm form, BindingResult result, Model model) {
		if(session.getAttribute("email") == null) {
			return "redirect:/mailInsert";
		}
		
		//バリデーションチェックによるエラーがあればユーザー登録画面に遷移
		if(result.hasErrors()) {
			return "register_user";
		}
		
		//パスワードと確認用パスワードが不一致の場合エラー文をリクエストスコープに格納してユーザー登録画面に遷移
		if(!(form.getPassword().equals(form.getConfirmPassword()))) {
			model.addAttribute("passwordNotMatchError", messageSource.getMessage("error.password.notmatch", null, "パスワードが違います", Locale.JAPAN));
			return "register_user";
		}
		
		//フォームの値をドメインにコピー
		User user = new User();
		BeanUtils.copyProperties(form, user);
		user.setEmail((String) session.getAttribute("email"));
		//郵便番号のハイフンを消してドメインにセット
		user.setZipcode(form.getZipcode().replace("-", ""));
		
		//emailが既に登録の場合はSQLで例外が発生するのでtry-catchを行う。
		//例外の際はエラー文をリクエストスコープに格納してユーザー登録画面に遷移
		try {
			userService.insert(user);
			session.removeAttribute("email");
			return "redirect:/toLogin";
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			model.addAttribute("emailRegistedError",messageSource.getMessage("error.email.duplicate", null, "登録済みのメールアドレスです", Locale.JAPAN));
			return "register_user";
		}
	}
}
