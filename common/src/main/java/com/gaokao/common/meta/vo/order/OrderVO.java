package com.gaokao.common.meta.vo.order;

import lombok.Data;

/**
 * @author Elvira0902
 * date:2021/08/11
 */
@Data
public class OrderVO {
    private Long id;

    private Long userId;

    private Long goodsId;

    private String totalPrice;

    private String realPrice;

    private Long createTime;

    private Long payTime;

    private String outTradeNo;

    private Integer payType;

    private String thirdPaySn;

    private Integer status;

    private String ip;
}
