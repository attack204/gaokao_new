package com.gaokao.common.meta.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author Elvira0902
 * date:  2021/8/11
 * desc: 订单相关
 *
 * //TODO 订单这块没有配置，先不做
 */

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_order")
public class Order{
    @Id
    private Long id;

    private Long userId;

    private Long goodsId;

    private Integer totalPrice;

    private Integer realPrice;

    private Long createTime;

    private Long payTime;

    private String outTradeNo;

    private Integer payType;

    private String thirdPaySn;

    private Integer status;

    private String ip;

}
