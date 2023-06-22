package com.gaokao.common.meta.vo.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * @author wyc-0705
 * date: 2021/8/23
 * desc: 订单提交参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubmitOrderParam extends PreOrderResult {

    /**
     * 备注
     */
    private String remark;

}