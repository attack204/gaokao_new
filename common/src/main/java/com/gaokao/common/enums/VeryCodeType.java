package com.gaokao.common.enums;

/**
 * Created on 2021/8/9.
 *
 * @author wyc
 */
public enum VeryCodeType {
    REG("reg", 60, 4, "注册");

    /**
     * 类型
     */
    private final String type;

    /**
     * 过期时间，以秒为单位
     */
    private final int ttl;

    /**
     * 验证码长度
     */
    private final int length;

    /**
     * 描述
     */
    private final String desc;

    VeryCodeType(String type, int ttl, int length, String desc) {
        this.type = type;
        this.ttl = ttl;
        this.length = length;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public int getTtl() {
        return ttl;
    }

    public int getLength() {
        return length;
    }

    public String getDesc() {
        return desc;
    }
}
