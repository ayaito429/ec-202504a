package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.common.CustomUserDetails;
import com.example.domain.User;
import com.example.form.EditProfileForm;
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

        EditProfileForm editProfileForm = new EditProfileForm(); // ← 修正
        editProfileForm.setName(user.getName());
        editProfileForm.setZipcode(user.getZipcode());
        editProfileForm.setAddress(user.getAddress());
        editProfileForm.setTelephone(user.getTelephone());

        model.addAttribute("editProfileForm", editProfileForm);
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
    public String updateProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Validated EditProfileForm editProfileForm,
        BindingResult bindingResult,
        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDetails.getUser());
            return "profile/profile_edit";
        }

        User user = userDetails.getUser();
        user.setName(editProfileForm.getName());
        user.setZipcode(editProfileForm.getZipcode());
        user.setAddress(editProfileForm.getAddress());
        user.setTelephone(editProfileForm.getTelephone());

        userService.update(user);
        return "redirect:/profile";
    }   
}
