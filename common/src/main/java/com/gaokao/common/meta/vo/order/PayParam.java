package com.gaokao.common.meta.vo.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gaokao.common.enums.PayType;
import com.gaokao.common.utils.LongJsonDeserializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
/**
 * @author wyc-0705
 * date: 2021/8/23
 * desc: 支付参数
 */
@Data
public class PayParam {
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long orderId;

    private PayType payType;

}
