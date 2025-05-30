package com.example.domain;

import java.util.List;

/**
 * カート内注文用のドメインクラス
 * 
 * @author shirota sho
 */
public class CartOrder {
    private Integer id;
    private Integer userId;
    private List<CartItem> orderItemList;

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public List<CartItem> getOrderItemList() {
        return orderItemList;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setOrderItemList(List<CartItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "CartOrder [id=" + id + ", userId=" + userId + ", orderItemList=" + orderItemList + "]";
    }
}
