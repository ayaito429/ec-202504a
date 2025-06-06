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
import com.example.domain.OrderTopping;
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
                Order existingCart = orders.get(0);
                Integer orderId = existingCart.getId();
                List<OrderItem> existingItems = existingCart.getOrderItemList();

                for (OrderItem newItem : cartOrder.getOrderItemList()) {
                    boolean merged = false;

                    for (OrderItem existingItem : existingItems) {
                        if (existingItem.getItemId().equals(newItem.getItemId())
                                && existingItem.getSize().equals(newItem.getSize())
                                && toppingsEqual(existingItem.getOrderTopping(), newItem.getOrderTopping())) {

                            int mergedQuantity = existingItem.getQuantity() + newItem.getQuantity();
                            orderService.addQuantity(existingItem.getId(), mergedQuantity);
                            merged = true;
                            break;
                        }
                    }

                    if (!merged) {
                        newItem.setOrderId(orderId);
                        orderService.insertOrderItem(newItem);
                    }
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

    private boolean toppingsEqual(List<OrderTopping> list1, List<OrderTopping> list2) {
        if (list1 == null || list2 == null)
            return false;
        if (list1.size() != list2.size())
            return false;

        List<Integer> ids1 = list1.stream().map(OrderTopping::getToppingId).sorted().toList();
        List<Integer> ids2 = list2.stream().map(OrderTopping::getToppingId).sorted().toList();

        return ids1.equals(ids2);
    }

}
