package com.gaokao.common.dao;

import com.gaokao.common.meta.po.OrderPay;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderPayDao extends PagingAndSortingRepository<OrderPay, Long> {
    OrderPay findByOrderId(Long orderId);

    @Transactional
    void deleteByOrderId(Long orderId);
}
