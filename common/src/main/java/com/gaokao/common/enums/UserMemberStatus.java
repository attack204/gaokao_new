package com.gaokao.common.enums;

/**
 * @author wyc
 * 用户状态
 * date 2021/8/2
 */
public enum UserMemberStatus {

        NORMAL(0, "正常"),
        LOCKED(1, "已锁定");

        private final Integer code;
        private final String desc;

        UserMemberStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

}
