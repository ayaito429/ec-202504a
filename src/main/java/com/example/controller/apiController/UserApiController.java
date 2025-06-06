package com.example.controller.apiController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Order;
import com.example.domain.User;
import com.example.enums.OrderStatus;
import com.example.exception.LoginFailedException;
import com.example.exception.UnauthorizedAccessException;
import com.example.exception.dto.OrderResponse;
import com.example.service.OrderService;
import com.example.service.UserService;

/**
 * 注文履歴画面に遷移するためのAPIコントローラー
 * 
 * @author aya_ito
 */
@CrossOrigin(origins = "*", methods = { RequestMethod.GET })
@RestController
@RequestMapping("/users")
public class UserApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
	private MessageSource messageSource;

    /**
     * ログイン情報から注文履歴を取得する
     * 
     * @param encoding エンコードされたメールアドレスとパスワード
     * @return 取得した注文履歴
     */
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponse>> getUserId(@RequestHeader("Authorization") String loginInfo,
            @PathVariable Integer userId) {

        // デコード
        String base64Credentials = loginInfo.substring("Basic ".length()).trim();
        String decoded = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);

        // メールアドレスとパスワードに分ける
        String[] loginInfoList = decoded.split(":");

        // ユーザー情報を取得
        User user = userService.login(loginInfoList[1], loginInfoList[0]);

        if (user == null) {
            throw new LoginFailedException(messageSource.getMessage("LoginFailedException", null, Locale.JAPAN));
        } else if (!userId.equals(user.getId())) {
            throw new UnauthorizedAccessException(messageSource.getMessage("UnauthorizedAccessException", null, Locale.JAPAN));
        }

        // 注文履歴を取得
        List<Order> orderList = orderService.findByOrder(user.getId());
        List<OrderResponse> responseList = new ArrayList<>();

        // Orderの値をOrderResponseにセット
        for (Order order : orderList) {
            OrderResponse orderResponse = new OrderResponse();
            
            orderResponse.setId(order.getId());
            orderResponse.setUserId(userId);
            // enumから値を取得
            OrderStatus status = OrderStatus.of(order.getStatus());
            orderResponse.setStatus(status.getValue());
            orderResponse.setTotalPrice(order.getTotalPrice());
            orderResponse.setOrderDate(order.getOrderDate());
            orderResponse.setDestinationName(order.getDestinationName());
            orderResponse.setDestinationEmail(order.getDestinationEmail());
            orderResponse.setDestinationZipcode(order.getDestinationZipcode());
            orderResponse.setDestinationAddress(order.getDestinationAddress());
            orderResponse.setDestinationTel(order.getDestinationTel());
            orderResponse.setDeliveryTime(order.getDeliveryTime());
            if (order.getPaymentMethod().equals(1)) {
                orderResponse.setPaymentMethod("代金引換");
            } else {
                orderResponse.setPaymentMethod("クレジットカード");
            }
            orderResponse.setOrderItemList(order.getOrderItemList());

            responseList.add(orderResponse);
        }

        return ResponseEntity.ok(responseList);
    }
}
