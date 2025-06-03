package com.example.enums;

/**
 * 注文ステータスのenumクラス
 * 
 * @author aya_ito
 */
public enum OrderStatus {

    PREORDER(0, "注文前"),
    UNPAID(1, "未入金"),
    PAID(2, "入金済"),
    SHIPPED(3, "発送済"),
    DELIVERED(4, "配達完了"),
    CANCELLED(9, "キャンセル");

    private final Integer key;
    private final String value;

    private OrderStatus(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    /**
     * 指定されたキーに対応するOrderStatusを返す
     * 
     * @param key 取得したいステータスのキー
     * @return 対応するOrderStatus。存在しなければnull
     */
    public static OrderStatus of(Integer key) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.key == key) {
                return status;
            }
        }
        return null;
    }

}
