package com.gaokao.common.meta.po;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_order_pay")
public class OrderPay {
    @Id
    private Long id;

    private Long orderId;

    private int status;

    private Integer payType;

    private Integer payMoney;

    private String outTradeNo;

    private String thirdPaySn;

    private Long payTime;
}
