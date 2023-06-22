package com.gaokao.common.meta.vo.order;

import lombok.Data;

@Data
public class PaymentInfoVO {

    private String merchantOrderId;         // 商户订单号
    private String merchantUserId;          // 商户方的发起用户的用户主键id
    private Integer amount;                 // 实际支付总金额（包含商户所支付的订单费邮费总额）
    private String qrCodeUrl;               // 二维码扫码地址

}
