package com.gaokao.common.utils;

import java.util.UUID;

public class RandomUtils {
    public static String randomNum(int length) {
        int rs = (int) ((Math.random() * 9 + 1) * Math.pow(10, length - 1));
        return String.valueOf(rs);
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
