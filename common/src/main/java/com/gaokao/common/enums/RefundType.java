package com.gaokao.common.enums;

public enum RefundType {
    WX_REFUND(1, "微信退款");
    private final int value;
    private final String desc;

    RefundType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
