package com.gaokao.common.meta.vo.order;

import com.gaokao.common.enums.PayType;
import lombok.Data;

/**
 * @author wyc-0705
 * date: 2021/8/23
 * desc: 订单支付结果
 */
@Data
public class PayResult {
    /**
     * 支付方法
     */
    private PayType payType;
    /**
     *时间戳
     */
    private String timeStamp;
    /**
     *生成签名的随机字符串
     */
    private String nonceStr;
    /**
     *预支付id
     */
    private String prepayId;
    /**
     *
     */
    private String paySign;
    /**
     *
     */
    private String signType;
    private String return_code;                // 返回状态码
    private String appId;                    // 公众账号ID
    private String mch_id;                    // 商户号
    private String nonce_str;                // 随机字符串
    private String result_code;                // 业务结果
    private int total_fee;                    // 总金额
    private String transaction_id;            // 微信支付订单号
    private String out_trade_no;            // 商户订单号
    private String time_end;                // 支付完成时间
    private String return_msg;                // 返回信息
    private String device_info;                // 设备号
    private String err_code;                // 错误代码
    private String err_code_des;            // 错误代码描述
    private String fee_type;                // 货币种类


}
