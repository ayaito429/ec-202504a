package com.example.domain;

import java.util.List;

public class CartItem {
	
	//商品Id
	private Integer itemId;
	//商品名
	private String name;
	//商品サイズ
	private String size;
	//商品画像
	private String imagePath;
	//トッピングのList
	private List<Topping> toppingList;
	//小計金額
//	private Integer subTotal;
	//数量
	private Integer quantity;
	//商品の元々の金額
	private Integer itemPrice;
	
	
	
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public List<Topping> getToppingList() {
		return toppingList;
	}
	public void setToppingList(List<Topping> topping) {
		this.toppingList = topping;
	}
	public Integer getSubTotal() {
		if(size.equals("M")) {
			return (this.getItemPrice()+(this.getToppingList().size() * 200)) * this.getQuantity();
		} else {
			return (this.getItemPrice()+(this.getToppingList().size() * 300)) * this.getQuantity();
		}		
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "CartItem [itemId=" + itemId + ", name=" + name + ", size=" + size + ", imagePath=" + imagePath
				+ ", toppingList=" + toppingList + ", subTotalPrice=" + this.getSubTotal() + ", area=" + quantity
				+ ", itemPrice=" + itemPrice + "]";
	}
	
}
