package com.gaokao.common.meta.vo.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gaokao.common.enums.RefundType;
import com.gaokao.common.utils.LongJsonDeserializer;
import lombok.Data;

@Data
public class RefundParam {
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long orderId;

    private Long userId;

    private RefundType refundType;
}
