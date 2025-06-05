package com.example.common;

import java.io.IOException;
import java.util.List;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * カスタムサービスクラス
 * 
 * @author shirota sho
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private HttpSession session;

    @Autowired
    private OrderService orderService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUserId();
        String role = userDetails.getUser().getRole();

        Order cartOrder = (Order) session.getAttribute("cartOrder");

        if (cartOrder != null) {
            cartOrder.setUserId(userId);
            Integer status = 0;

            List<Order> orders = orderService.findByStatus(userId, status);
            if (orders == null) {
                orders = Collections.emptyList();
            }

            if (!orders.isEmpty()) {
                Integer orderId = orders.get(0).getId();
                for (OrderItem orderItem : cartOrder.getOrderItemList()) {
                    orderItem.setOrderId(orderId);
                    orderService.insertOrderItem(orderItem);
                }
            } else {
                Integer newOrderId = orderService.insert(cartOrder);
                for (OrderItem orderItem : cartOrder.getOrderItemList()) {
                    orderItem.setOrderId(newOrderId);
                    orderService.insertOrderItem(orderItem);
                }
            }

            session.removeAttribute("cartOrder");
        }

        String redirectUrl;
        if ("ADMIN".equals(role)) {
            redirectUrl = request.getContextPath() + "/admin";
        } else {
            redirectUrl = request.getContextPath() + "/showList";
        }

        response.sendRedirect(redirectUrl);
    }

}
