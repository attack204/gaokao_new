package com.gaokao.common.dao;

import com.gaokao.common.meta.po.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderDao extends PagingAndSortingRepository<Order, Long>  {

    Order findByIdAndGoodsId(Long id, Long shopId);

    Page<Order> findAllByStatus(int status, Pageable pageable);

    Order findByOutTradeNo(String outTradeNo);

    Page<Order> findAll(Pageable pageable);

    void deleteById(Long orderId);

    Page<Order> findAllByIdAndUserId(Long Id,  Long userId, Pageable pageable);

    Page<Order> findAllByUserId(Long userId, Pageable pageable);

    Page<Order> findAllById(Long Id,  Pageable pageable);

}

