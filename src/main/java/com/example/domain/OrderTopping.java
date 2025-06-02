package com.example.domain;

/**
 * OrderToppingのドメイン
 * 
 * @author naramasato
 *
 */
public class OrderTopping {

	// id
	private Integer id;
	// トッピングid
	private Integer toppingId;
	// 注文商品id
	private Integer orderItemId;
	// topping情報
	private Topping topping;
	// 注文時の金額
	private Integer price;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getToppingId() {
		return toppingId;
	}

	public void setToppingId(Integer toppingId) {
		this.toppingId = toppingId;
	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Topping getTopping() {
		return topping;
	}

	public void setTopping(Topping topping) {
		this.topping = topping;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderTopping [id=" + id + ", toppingId=" + toppingId + ", orderItemId=" + orderItemId + ", topping="
				+ topping + ", price=" + price + "]";
	}
}
