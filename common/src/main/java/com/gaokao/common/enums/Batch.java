package com.gaokao.common.enums;

/**
 * @author attack204
 * date:  2021/7/21
 * email: 757394026@qq.com
 */

/**
 * 录取批次
 */
public enum Batch {
    NOTMAL_ONE(1, "普通类一段"),

    NOTMAL_TWO(2, "普通类二段");

    private final int code;

    private final String desc;

    Batch(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }
}
