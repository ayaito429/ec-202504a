package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.common.CustomUserDetails;
import com.example.domain.User;
import com.example.form.InsertForm;
import com.example.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;


/**
 * プロフィール画面に遷移するためのコントローラー
 * 
 * @author yukisato
 */
@Controller
@RequestMapping("")
public class ProfileController {

    @Autowired
    private  UserService userService;
    
    /**
     * プロフィール画面に遷移
     * 
     * @return プロフィール画面のテンプレート名
     */
    @RequestMapping("/profile")
    public String showProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("user", userDetails.getUser());
        return "profile/profile";
    }

    /**
     * プロフィール編集画面に遷移
     * 
     * @return プロフィール編集画面のテンプレート名
     */
    @RequestMapping("/editProfile")
    public String editProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        InsertForm insertForm = new InsertForm();
        insertForm.setName(user.getName());
        insertForm.setZipcode(user.getZipcode());
        insertForm.setAddress(user.getAddress());
        insertForm.setTelephone(user.getTelephone());

        model.addAttribute("insertForm", insertForm);
        model.addAttribute("user", userDetails.getUser());
        return "profile/profile_edit";
    }

    /**
     * プロフィールを更新
     * 
     * @param userDetails 認証されたユーザーの詳細
     * @param insertForm 更新するプロフィール情報
     * @return プロフィール画面へのリダイレクト
     */
    @PostMapping("/updateProfile")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails, InsertForm insertForm) {
        User user = userDetails.getUser();
        user.setName(insertForm.getName());
        user.setZipcode(insertForm.getZipcode());
        user.setAddress(insertForm.getAddress());
        user.setTelephone(insertForm.getTelephone());

        userService.update(user);
        return "redirect:/profile";

    }      
}
