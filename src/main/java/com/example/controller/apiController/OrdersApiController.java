package com.example.controller.apiController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Order;
import com.example.domain.User;
import com.example.exception.handle.LoginFailedException;
import com.example.exception.handle.UnauthorizedAccessException;
import com.example.service.OrderService;
import com.example.service.UserService;

/**
 * 注文履歴画面に遷移するためのAPIコントローラー
 * 
 * @author aya_ito
 */
@RestController
@RequestMapping("/users")
public class OrdersApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    /**
     * ログイン情報から注文履歴を取得する
     * 
     * @param encoding エンコードされたメールアドレスとパスワード
     * @return 取得した注文履歴
     */
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<Order>> getUserId(@RequestHeader("Authorization") String encoding,
            @PathVariable Integer userId) {

        // デコード
        String base64Credentials = encoding.substring("Basic ".length()).trim();
        String decoded = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
        System.out.println("デコードした文字列：" + decoded);

        // メールアドレスとパスワードに分ける
        String[] loginInfo = decoded.split(":");

        // ユーザー情報を取得
        User user = userService.login(loginInfo[1], loginInfo[0]);

        if (user == null) {
            throw new LoginFailedException("ログイン情報が正しくありません");
        } else if (!userId.equals(user.getId())) {
            throw new UnauthorizedAccessException("不正なリクエストです。");
        }

        // 注文履歴を取得
        List<Order> orderList = orderService.findByOrder(user.getId());
        return ResponseEntity.ok(orderList);
    }
}
