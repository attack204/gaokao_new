package com.gaokao.common.enums;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
public enum BusinessCode {
    SUCCESS(200, "成功"),

    UNAUTHORIZED(401, "请先登录"),

    FORBIDDEN(403, "Forbidden"),

    FAIL(500, "失败");

    private final int code;

    private final String desc;


    BusinessCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
