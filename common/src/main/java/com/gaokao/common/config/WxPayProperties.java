package com.gaokao.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
    /**
     * 设置微信公众号或者小程序等的appid
     */

    private String appId;

    /**
     * 微信支付商户号
     */

    private String mchId;

    /**
     * 微信支付密钥
     */

    private String mchKey;


    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */

    private String keyPath;



    private String callback;



    private boolean sanbox;


    /**
     * 交易方式
     */
    private String tradeType;

    /**
     * 通知地址
     */
    private String notify_url;

    private String UnifiedOrderUrl="https://api.mch.weixin.qq.com/v3/pay/transactions/native";
}
