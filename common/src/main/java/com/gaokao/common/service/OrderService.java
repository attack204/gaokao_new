package com.gaokao.common.service;

import com.gaokao.common.meta.po.OrderRefund;
import com.gaokao.common.meta.vo.order.*;
import com.gaokao.common.meta.vo.user.UserMemberVO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Map;

/**
 * @author wyc-0705
 * date: 2021/8/23
 * desc: 订单
 */
public interface OrderService {


    @Transactional(propagation = Propagation.REQUIRED)//默认隔离级别
    Long generateOrder(Long userId) throws Exception;

    @Transactional(propagation = Propagation.REQUIRED)//默认隔离级别
    String saveOrder(Long userId) throws Exception;

    //查询订单支付状态
    Map<String, String> queryPayStatus(String orderNo);

    /**
     * 1: 提交订单
     */
    Long submit(SubmitOrderParam param, Long userId);

//    /**
//     * 2: 支付订单
//     */
//    PayResult pay(PayParam param, String clientIp, Long userId);

    /**
     * 2.1: 支付回调
     */
    String wxPayNotify(String content);

    /**
     * 3: 关闭订单
     */
    void close(Long orderId, Long userId);

    /**
     * 4：退款
     */
    void applyRefund(RefundParam refundParam);

    /**
     * 4.1: 退款回调
     */
    String refundCallback(String content);

    /**
     * 4: 取消订单
     */
    void cancel(Long orderId, Long userId);

    /**
     * 完成订单
     */
    void completeOrder(Long orderId, Long userId);

    /**
     * 根据订单id获取订单详情
     */
    OrderVO getByOrderId(Long id, Long userId);

    /**
     * 获取历史订单总量
     * @return 历史订单总量
     */
    Long allOrderNum();

    /**
     * 获取今日总收入
     * @param todayZeroTime 今日0点时间
     */
    int todaySaleVolume(Long todayZeroTime);

    /**
     * 拒绝退款
     */
    void rejectRefund(Long orderId, String reasons);

    /**
     * 查询所有订单
     * @param
     */

    Page<OrderVO> list(Long orderId,  Long userId, int page, int size);


}
