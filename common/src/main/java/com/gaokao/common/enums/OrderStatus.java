package com.gaokao.common.enums;

public enum OrderStatus {
    ALL(-1, "所有订单"),

    READY_FOR_PAY(1, "待支付"),

    CANCELED(2, "已取消"),

    PAID_SUCCESS(5, "支付成功"),

    APPLY_FOR_REFUND(14, "退款申请中"),

    REFUND_WAITING_NOTIFY(15, "已退款，待确认"),

    REFUND_REJECT(17,"商家拒绝退款"),

    REFUND_SUCCESS(20, "退款成功"),

    CLOSED(25, "已关闭"),

    COMPLETED(30, "订单已完成");

    private final int value;
    private final String desc;

    OrderStatus(int value, String desc) {
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
