package com.example.domain;

import java.util.List;

public class CartItem {

	// 商品Id
	private Integer itemId;
	// 商品名
	private String name;
	// 商品サイズ
	private String size;
	// 商品画像
	private String imagePath;
	// トッピングのList
	private List<CartTopping> cartToppingList;
	// 小計金額
	private Integer subTotal;
	// 数量
	private Integer quantity;
	// 商品の金額
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

	public List<CartTopping> getCartToppingList() {
		return cartToppingList;
	}

	public void setCartToppingList(List<CartTopping> cartToppingList) {
		this.cartToppingList = cartToppingList;
	}

	public Integer getSubTotal() {
		Integer totaleToppingPrice = 0;
		for (CartTopping cartTopping : cartToppingList) {
			totaleToppingPrice += cartTopping.getPrice();
		}
		return (this.getItemPrice() + totaleToppingPrice) * this.getQuantity();
	}

	public void setSubTotal(Integer subTotal) {
		this.subTotal = subTotal;
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
				+ ", toppingList=" + cartToppingList + ", subTotalPrice=" + subTotal + ", area=" + quantity
				+ ", itemPrice=" + itemPrice + "]";
	}

}
