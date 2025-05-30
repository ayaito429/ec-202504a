package com.example.enums;

public enum Status {

    PREORDER(0, "注文前"),
    UNPAID(1, "未入金"),
    PAID(2, "入金済"),
    SHIPPED(3, "発送済"),
    DELIVERED(4, "配達完了"),
    CANCELLED(9, "キャンセル");

    private final Integer key;
    private final String value;

    private Status(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static Status of(Integer key) {
        for (Status status : Status.values()) {
            if (status.key == key) {
                return status;
            }
        }
        throw new IndexOutOfBoundsException("値が存在しません");
    }

}
