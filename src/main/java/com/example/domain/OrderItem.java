package com.example.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderItem {

	// id
	private Integer id;
	// 商品id
	private Integer itemId;
	// orderのid
	private Integer orderId;
	// 数量
	private Integer quantity;
	// サイズ
	private String size;
	// 小計
	private Integer subTotal;
	// item
	private Item item;
	// 注文したトッピングのList
	private List<OrderTopping> orderTopping = new ArrayList<>();
	// 注文時の金額
	private Integer itemPrice;

	// ゲッターとセッター
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<OrderTopping> getOrderTopping() {
		return orderTopping;
	}

	public void setOrderTopping(List<OrderTopping> orderTopping) {
		this.orderTopping = orderTopping;
	}

	public Integer getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", itemId=" + itemId + ", orderId=" + orderId + ", quantity=" + quantity
				+ ", size=" + size + ", item=" + item + ", ordertopping=" + orderTopping + ", subTotal=" + subTotal
				+ ", itemPrice=" + itemPrice + "]";
	}

	public Integer getSubTotal() {
		Integer totaleToppingPrice = 0;
		for (OrderTopping topping : orderTopping) {
			totaleToppingPrice += topping.getPrice();
		}
		return (this.getItemPrice() + totaleToppingPrice) * this.getQuantity();
	}

	public void setSubTotal(Integer subTotal) {
		this.subTotal = subTotal;
	}
}
