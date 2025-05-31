package com.example.domain;

import java.util.List;

public class CartItem {

	// id
	private Integer id;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", itemId=" + itemId + ", name=" + name + ", size=" + size + ", imagePath="
				+ imagePath + ", cartToppingList=" + cartToppingList + ", subTotal=" + getSubTotal() + ", quantity="
				+ quantity + ", itemPrice=" + itemPrice + "]";
	}

}
