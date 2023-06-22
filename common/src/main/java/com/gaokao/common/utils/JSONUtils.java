package com.gaokao.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
public class JSONUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return objectMapper;
    }
}
