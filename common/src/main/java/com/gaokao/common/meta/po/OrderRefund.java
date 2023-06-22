package com.gaokao.common.meta.po;

import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Elvira0902
 * date: 2021.8.12
 * desc: 订单退款相关
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_order_refund")
public class OrderRefund {

    @Id
    private Long orderId;

    private Integer refundType;

    private Integer refundMoney;

    private Integer status;

    private String thirdRefundSn;

    private Long refundTime;

    private String note;
}
