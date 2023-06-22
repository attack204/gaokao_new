package com.gaokao.common.enums;

/**
 * @author attack204
 * date:  2021/7/21
 * email: 757394026@qq.com
 */
public enum SubjectRestrictionType {

    NONE(0, "没有限制"),

    ONE_MUST(1, "必选一科"),

    TWO_MUST(2, "必选两科"),

    THREE_MUST(3, "必选三科"),

    ONE_OF_TWO(4, "二选一"),

    ONE_OF_THREE(5, "三选一"),

    TWO_OF_THREE(6, "三选二");

    private final int code;

    private final String desc;

    SubjectRestrictionType(int code, String desc) {
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
