package com.gaokao.common.enums;
/**
 * @author wyc-0705
 * date: 2021/8/23
 * desc: 订单支付方式
 */
public enum  PayType {
    WX_NATIVE_PAY(1, "微信NATIVE支付");
    private final int value;
    private final String desc;

    PayType(int value, String desc) {
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
