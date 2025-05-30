package com.example.domain;

/**
 * カート内注文のトッピング用のドメインクラス
 * 
 * @author shirota sho
 */
public class CartTopping {
    // id
    private Integer id;
    // トッピング名
    private String name;
    // 価格
    private Integer price;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CartTopping [id=" + id + ", name=" + name + ", price=" + price + "]";
    }

}
