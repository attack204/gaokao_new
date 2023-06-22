package com.gaokao.common.utils;

import java.text.DecimalFormat;

public class NumberUtils {
    /**
     * 价格由分转化为元,保留两位小数
     * @param fenPrice 以分为单位的价格
     * @return 以元为单位的价格
     */
    public static String priceFormatToYuan(int fenPrice) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((float) fenPrice / 100);
    }

    /**
     * 价格由元转化为分,去除多余小数
     * @param yuanPrice 以元为单位的价格
     * @return 以分为单位的价格
     */
    public static Integer priceFormatToFen(double yuanPrice) {
        DecimalFormat df = new DecimalFormat("#");
        String str= df.format( yuanPrice * 100);
        if (str.contains(".")){
            int indexOf = str.indexOf(".");
            str = str.substring(0, indexOf);
        }
        return Integer.parseInt(str);
    }

    public static Integer kilometreFormatToMetres(int kilometre) {
        return kilometre * 1000;
    }

    public static Integer metresFormatToKilometre(int metres) {
        return metres / 1000;
    }
}
