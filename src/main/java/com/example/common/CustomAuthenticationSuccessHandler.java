package com.example.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
 * カスタムハンドラークラス
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

        Order sessionCartOrder = (Order) session.getAttribute("cartOrder");

        if (sessionCartOrder != null) {
            Integer userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
            sessionCartOrder.setUserId(userId);
            Integer status = 0;

            List<Order> existingOrders = orderService.findByStatus(userId, status);
            Order targetOrder;
            List<OrderItem> mergedItems = new ArrayList<>();

            if (existingOrders != null && !existingOrders.isEmpty()) {
                targetOrder = existingOrders.get(0);
                mergedItems.addAll(targetOrder.getOrderItemList());

                for (OrderItem sessionItem : sessionCartOrder.getOrderItemList()) {
                    boolean isMerged = false;

                    for (OrderItem existingItem : mergedItems) {
                        if (existingItem.getItemId().equals(sessionItem.getItemId())
                                && existingItem.getSize().equals(sessionItem.getSize())
                                && toppingsEqual(existingItem.getOrderTopping(), sessionItem.getOrderTopping())) {

                            int newQuantity = existingItem.getQuantity() + sessionItem.getQuantity();
                            existingItem.setQuantity(newQuantity);

                            orderService.deleteForCartItem(existingItem.getId());
                            sessionItem.setOrderId(targetOrder.getId());
                            sessionItem.setQuantity(newQuantity);
                            orderService.insertOrderItem(sessionItem);

                            isMerged = true;
                            break;
                        }
                    }

                    if (!isMerged) {
                        sessionItem.setOrderId(targetOrder.getId());
                        orderService.insertOrderItem(sessionItem);
                        mergedItems.add(sessionItem);
                    }
                }
            } else {
                Integer newOrderId = orderService.insert(sessionCartOrder);
                for (OrderItem sessionItem : sessionCartOrder.getOrderItemList()) {
                    sessionItem.setOrderId(newOrderId);
                    orderService.insertOrderItem(sessionItem);
                    mergedItems.add(sessionItem);
                }
                targetOrder = sessionCartOrder;
                targetOrder.setId(newOrderId);
            }

            int totalPrice = 0;
            for (OrderItem item : mergedItems) {
                int itemPrice = item.getSize().equals("M") ? item.getItem().getPriceM() : item.getItem().getPriceL();
                item.setItemPrice(itemPrice);
                totalPrice += itemPrice * item.getQuantity();

                for (OrderTopping topping : item.getOrderTopping()) {
                    int toppingPrice = item.getSize().equals("M") ? topping.getTopping().getPriceM()
                            : topping.getTopping().getPriceL();
                    topping.setPrice(toppingPrice);
                    totalPrice += toppingPrice * item.getQuantity();
                }
            }

            targetOrder.setTotalPrice(totalPrice);
            orderService.update(targetOrder);

            session.removeAttribute("cartOrder");
        }

        response.sendRedirect("/showList");
    }

    private boolean toppingsEqual(List<OrderTopping> list1, List<OrderTopping> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        Set<Integer> toppingIds1 = list1.stream().map(t -> t.getTopping().getId()).collect(Collectors.toSet());
        Set<Integer> toppingIds2 = list2.stream().map(t -> t.getTopping().getId()).collect(Collectors.toSet());
        return toppingIds1.equals(toppingIds2);
    }
}
