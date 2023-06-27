package com.example.domain;

import java.util.List;

public class OrderItem {

	//id
	private Integer id;
	//商品id
	private Integer itemId;
	//orderのid
	private Integer orderId;
	//数量
	private Integer quantity;
	//サイズ
	private String size;
	//小計
//	private Integer subTotal;
	//item
	private Item item;
	//注文したトッピングのList
	private List<OrderTopping> orderTopping;
	
	//ゲッターとセッター
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
	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", itemId=" + itemId + ", orderId=" + orderId + ", quantity=" + quantity
				+ ", size=" + size + ", item=" + item + ", ordertopping=" + orderTopping
				+ "]";
	}
	
	public Integer getSubTotal() {
		if(size.equals("M")) {
			return (item.getPriceM()+(this.getOrderTopping().size() * 200)) * this.getQuantity();
		} else {
			return (item.getPriceL()+(this.getOrderTopping().size() * 300)) * this.getQuantity();
		}		
	}
}
