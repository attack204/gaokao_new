package com.gaokao.common.service;

public interface IdService {
    Long genOrderId(Long userId);

    Long genOrderPayId(Long orderId);
}
