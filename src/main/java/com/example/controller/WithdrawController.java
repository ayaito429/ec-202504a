package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.common.CustomUserDetails;
import com.example.domain.Order;
import com.example.domain.User;
import com.example.enums.OrderStatus;
import com.example.service.OrderService;
import com.example.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * 退会処理を行うコントローラー
 * 
 * @author yukisato
 */
@Controller
@RequestMapping("/withdraw")
public class WithdrawController {
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    /**
     * 退会確認画面を表示する
     * 
     * @return 退会確認画面のテンプレート名
     */
    @RequestMapping("/confirm")
    public String showWithdrawConfirm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        // カートに商品があるかを確認（trueならカートに商品あり）
        boolean hasCartItems = orderService.hasCartItems(user.getId());

        model.addAttribute("hasCartItems", hasCartItems);
        return "withdraw/confirm";
    }

    /**
     * 退会処理を行う
     * 
     * @param userDetails ログイン中のユーザー情報
     * @param redirectAttributes リダイレクト先でエラーメッセージを渡すため
     * @return 退会完了画面のテンプレート名
     */
    @PostMapping("/execute")
    public String executeWithdraw(@AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        User user = userDetails.getUser();

        // 未処理の注文があるかチェック（status 0〜3：未処理/進行中）
        List<Order> orderList = orderService.findByUserId(user.getId());
        boolean hasPendingOrders = orderList.stream()
            .map(order -> OrderStatus.of(order.getStatus()))
            .anyMatch(status ->
                status == OrderStatus.UNPAID ||
                status == OrderStatus.PAID ||
                status == OrderStatus.SHIPPED
            );

        if (hasPendingOrders) {
            redirectAttributes.addFlashAttribute("error", "未処理の注文があります。退会できません。");
            return "redirect:/withdraw/confirm";
        }

        // カート（status = 0）の注文をキャンセル状態（status = 9）に更新
        orderService.cancelAllCarts(user.getId());

        // ユーザーのステータスを「退会状態（9）」に更新（論理削除）
        userService.withdrawUser(user.getId());

        // セッション破棄
        session.invalidate();

        return "redirect:/withdraw/completed";
    }

    /**
     * 退会完了画面を表示する
     * 
     * @return 退会完了画面のテンプレート名
     */
    @RequestMapping("/completed")
    public String showWithdrawCompleted() {
        return "withdraw/completed";
    }
}
