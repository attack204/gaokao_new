package com.gaokao.common.meta.vo.common;

import lombok.Data;

/**
 * @author attack204
 * date:  2021/7/20
 * email: 757394026@qq.com
 */
@Data
public class NameValuePair {
    private String name;
    private Object value;

    public NameValuePair(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
