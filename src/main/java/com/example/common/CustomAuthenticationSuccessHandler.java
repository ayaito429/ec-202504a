package com.example.common;

import java.io.IOException;
import java.util.List;

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

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private HttpSession session;

    @Autowired
    private OrderService orderService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // セッションから cartOrder を取得
        Order cartOrder = (Order) session.getAttribute("cartOrder");

        if (cartOrder != null) {
            Integer userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
            cartOrder.setUserId(userId);
            Integer status = 0;
            List<Order> orders = orderService.findByStatus(userId, status);
            if (!orders.isEmpty() && orders.get(0) != null) {
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

        response.sendRedirect("/showList");
    }
}
