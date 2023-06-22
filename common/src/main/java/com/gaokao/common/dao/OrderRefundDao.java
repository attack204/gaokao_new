package com.gaokao.common.dao;

import com.gaokao.common.meta.po.Order;
import com.gaokao.common.meta.po.OrderRefund;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRefundDao  extends PagingAndSortingRepository<OrderRefund, Long> {
   OrderRefund findByOrderId(Long orderId);
}
