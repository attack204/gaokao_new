package com.gaokao.common.enums;

/**
 * @author attack204
 * date:  2021/7/21
 * email: 757394026@qq.com
 */
public enum Subject {
    PHYSICS(1, "物理"),

    CHEMISTRY(2, "化学"),

    BIOLOGY(3, "生物"),

    HISTORY(4, "历史"),

    GEOGRAPHY(5, "地理"),

    POLITICS(6, "政治");

    private final int code;

    private final String desc;


    Subject(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(int code){
        Subject[] subjects = values();
        for(Subject subject : subjects){
            if(subject.getCode() == code)
                return subject.getDesc();
        }
        return null;
    }
}
