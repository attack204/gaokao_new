package com.gaokao.common.enums;

/**
 * @author attack204
 * date:  2021/7/21
 * email: 757394026@qq.com
 */

/**
 * 招生类型
 */
public enum RecruitType {

    NORMAL(1, "普通批"),

    FOREIGN_COOPERATION(2, "中外合办"),

    ENTERPRISE_COOPERATION(3, "校企合办");

    private final int code;

    private final String desc;

    RecruitType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

}
