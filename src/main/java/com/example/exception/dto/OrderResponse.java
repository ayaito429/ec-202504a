package com.example.exception.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.example.domain.OrderItem;

/**
 * OrderResponseのドメイン
 * 
 * @author aya_ito
 *
 */
public class OrderResponse {

    // id
    private Integer id;
    // ユーザーid
    private Integer userId;
    // 状態
    private String status;
    // 合計金額
    private Integer totalPrice;
    // 注文日
    private Date orderDate;
    // 宛先氏名
    private String destinationName;
    // 宛先Eメール
    private String destinationEmail;
    // 宛先郵便番号
    private String destinationZipcode;
    // 宛先住所
    private String destinationAddress;
    // 宛先TEL
    private String destinationTel;
    // 配達時間
    private Timestamp deliveryTime;
    // 支払方法
    private String paymentMethod;

    // OrderItemのリスト
    private List<OrderItem> orderItemList;

    // ゲッターとセッター
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationEmail() {
        return destinationEmail;
    }

    public void setDestinationEmail(String destinationEmail) {
        this.destinationEmail = destinationEmail;
    }

    public String getDestinationZipcode() {
        return destinationZipcode;
    }

    public void setDestinationZipcode(String destinationZipcode) {
        this.destinationZipcode = destinationZipcode;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDestinationTel() {
        return destinationTel;
    }

    public void setDestinationTel(String destinationTel) {
        this.destinationTel = destinationTel;
    }

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", userId=" + userId + ", status=" + status + ", totalPrice=" + totalPrice
                + ", orderDate=" + orderDate + ", destinationName=" + destinationName + ", destinationEmail="
                + destinationEmail + ", destinationZipcode=" + destinationZipcode + ", destinationAddress="
                + destinationAddress + ", destinationTel=" + destinationTel + ", deliveryTime=" + deliveryTime
                + ", paymentMethod=" + paymentMethod + ", orderItemList=" + orderItemList + "]";
    }

}
