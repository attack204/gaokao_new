package com.gaokao.common.service;

import com.alibaba.fastjson.JSON;
import com.gaokao.common.config.WxPayProperties;
import com.gaokao.common.dao.OrderDao;
import com.gaokao.common.dao.OrderPayDao;
import com.gaokao.common.dao.OrderRefundDao;
import com.gaokao.common.dao.UserMemberDao;
import com.gaokao.common.enums.OrderStatus;
import com.gaokao.common.enums.PayType;
import com.gaokao.common.exceptions.BusinessException;
import com.gaokao.common.meta.bo.H5SceneInfo;
import com.gaokao.common.meta.po.*;
import com.gaokao.common.meta.vo.order.*;
import com.gaokao.common.meta.vo.user.UserMemberVO;
import com.gaokao.common.utils.*;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayOrderCloseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserMemberDao userMemberDao;

    @Autowired
    private IdService idService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private OrderPayDao orderPayDao;

    @Autowired
    private OrderRefundDao orderRefundDao;

    @Autowired
    private WxPayService wxService;

    @Autowired
    private WxPayProperties wxPayProperties;

    private final static String RECEIVE_ORDER = "YES";

    private final static String NOT_RECEIVE_ORDER = "NO";


    @Override
    @Transactional(propagation = Propagation.REQUIRED)//默认隔离级别
    public Long generateOrder(Long userId) throws Exception {
        //获取ip 模拟一个假的ip
        //String ip = IpUtils.getIpAddr(request);
        String ip = "120.25.1.43";
        Order order = new Order();
        order.setIp(ip);
        //查询用户信息
        UserMember user = userMemberDao.findUserMemberById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在") ;
        }
        //已经是vip
        if (user.isVipIsOrNot() == true) {
            throw new BusinessException("用户已经是vip");
        }
        //生成订单
        String outTradeNo = RandomUtils.randomUUID();
        order.setUserId(userId);//用户
        order.setStatus(OrderStatus.READY_FOR_PAY.getValue());//待支付
        order.setId(1L);//订单号
        order.setCreateTime(System.currentTimeMillis());//创建时间
        order.setOutTradeNo(outTradeNo);//对外订单号
        order.setPayType(PayType.WX_NATIVE_PAY.getValue());//支付方式为微信支付
        order.setThirdPaySn("11");
        order.setRealPrice(1);//todo
        order.setGoodsId(1L);//todo
        order.setPayTime(System.currentTimeMillis());
        order.setTotalPrice(1);
        //保存订单
        orderDao.save(order);

        return order.getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)//默认隔离级别
    public String saveOrder(Long orderId) throws Exception {
        Order order = orderDao.findById(orderId).orElse(null);
        //发送请求
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder().build();
        request.setAppid("wx6c3f8b761f7a1e03");//公众号AppId
        request.setMchId(wxPayProperties.getMchId());//商户ID
        request.setOutTradeNo(order.getOutTradeNo());//订单流水号
        request.setTradeType("NATIVE");//交易类型 扫码支付
        request.setBody("鲁济名师助力每一位考生的梦想");//商品描述
        request.setTotalFee(1);//商品金额
        request.setSpbillCreateIp(order.getIp());//终端IP
        request.setNotifyUrl(wxPayProperties.getNotify_url());//通知地址
        request.setProductId("1");

        //创建sign
        SortedMap<String,String> params = new TreeMap<>();
        params.put("appid","wx6c3f8b761f7a1e03");//公众号AppId
        params.put("mch_id",wxPayProperties.getMchId());//商户ID
        params.put("out_trade_no",order.getOutTradeNo());//订单流水号
        params.put("trade_type",wxPayProperties.getTradeType());//交易类型 扫码支付
        params.put("body","111");//商品描述
        params.put("total_fee","1");//商品金额
        params.put("spbill_create_ip",order.getIp());//终端IP
        params.put("notify_url",wxPayProperties.getNotify_url());//通知地址
        String sign = WXPayUtil.createSign(params,wxPayProperties.getMchKey());
        params.put("sign",sign);



        request.setSign(sign);
        WxPayUnifiedOrderResult payUnifiedOrderResult;
        try {
            payUnifiedOrderResult = this.wxService.unifiedOrder(request);
        } catch (Exception e) {
            log.error("[pay] pay failed.request={}", request, e);
            throw new BusinessException("微信支付失败");
        }
        //生成二维码
        return payUnifiedOrderResult.getCodeURL();
    }

    //查询订单支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx6c3f8b761f7a1e03");
            m.put("mch_id", "1613925940");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", com.github.wxpay.sdk.WXPayUtil.generateNonceStr());

            //2 发送httpclient
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(com.github.wxpay.sdk.WXPayUtil.generateSignedXml(m,"Ljmswxfd24dcdd82129aaa0123456789"));
            client.setHttps(true);
            client.post();

            //3 得到请求返回内容
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map再返回
            return resultMap;
        }catch(Exception e) {
            return null;
        }
    }


    @Override
    public Long submit(SubmitOrderParam param, Long userId) {
        Long orderId = idService.genOrderId(userId);
        Long currentTime = System.currentTimeMillis();
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setTotalPrice(param.getTotalPrice());
        order.setStatus(OrderStatus.READY_FOR_PAY.getValue());
        order.setCreateTime(currentTime);
        return orderId;
    }



    @Override
    public String wxPayNotify(String content) {
        try {
            WxPayOrderNotifyResult notifyResult = this.wxService.parseOrderNotifyResult(content);
            String outTradeNo = notifyResult.getOutTradeNo();
            Order order = orderDao.findByOutTradeNo(outTradeNo);
            if (order == null) {
                throw new BusinessException("订单不存在");
            }
            if (order.getStatus() != OrderStatus.READY_FOR_PAY.getValue()) {
                throw new BusinessException("订单状态不对");
            }
            order.setStatus(OrderStatus.PAID_SUCCESS.getValue());
            orderDao.save(order);

        } catch (Exception e) {
            throw new BusinessException("回调失败:", e);
        }
        return WxPayNotifyResponse.success("成功");
    }

    @Override
    public void completeOrder(Long orderId, Long userId) {
        Order order = orderDao.findById(orderId).orElse(null);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != OrderStatus.PAID_SUCCESS.getValue()) {
            throw new BusinessException("订单状态不对");
        }
        order.setStatus(OrderStatus.COMPLETED.getValue());
        orderDao.save(order);
        //完成之后变为vip
        UserMember userMember=userMemberDao.findUserMemberById(userId);
        userMember.setVipIsOrNot(true);
        userMemberDao.save(userMember);

    }

    @Override
    public void close(Long orderId, Long userId) {
        Order order = orderDao.findById(orderId).orElse(null);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != OrderStatus.READY_FOR_PAY.getValue()) {
            throw new BusinessException("订单状态不对");
        }
        OrderPay orderPay = orderPayDao.findByOrderId(orderId);
        if (orderPay == null) {
            throw new BusinessException("该订单没有关联的支付单！");
        }
        if (orderPay.getStatus() != OrderStatus.READY_FOR_PAY.getValue()) {
            throw new BusinessException("订单状态不对");
        }

        //先删除支付订单记录表中的记录
        orderPayDao.deleteByOrderId(orderId);

        WxPayOrderCloseResult payOrderCloseResult;
        WxPayOrderCloseRequest wxPayOrderCloseRequest = WxPayOrderCloseRequest.newBuilder()
                .outTradeNo(orderPay.getOutTradeNo())
                .build();
        try {
            payOrderCloseResult = this.wxService.closeOrder(wxPayOrderCloseRequest);
        } catch (Exception e) {
            log.error("[close] close failed.request={}", wxPayOrderCloseRequest, e);
            throw new BusinessException("关闭订单失败:" + e);
        }

        order.setStatus(OrderStatus.CLOSED.getValue());
        orderDao.save(order);
    }

    @Override
    public void applyRefund(RefundParam refundParam) {
        // 1.1 获取要退款的订单
        // 通过传入的退款参数中的orderId，利用orderDao实现查找要退款的订单
        Order order = orderDao.findById(refundParam.getOrderId()).orElse(null);
        // 1.2 获取订单后，需对获取的订单进行检查
        // a. 检查订单是否存在，检查订单对应用户与退款参数中的用户是不是同一个
        if (order == null || !order.getUserId().equals(refundParam.getUserId())) {
            throw new BusinessException("订单不存在");
        }
        // b.检查订单状态是否是申请退款状态
        if (order.getStatus() != OrderStatus.APPLY_FOR_REFUND.getValue()) {
            throw new BusinessException("订单状态不对");
        }
        OrderPay orderPay = orderPayDao.findByOrderId(refundParam.getOrderId());
        // 2.2 检查获取的支付信息是否有问题
        // a.检查订单支付信息是否为空
        if (orderPay ==null) {
            throw new BusinessException("该订单没有关联的支付单！");
        }
        // b.检查支付信息状态是否是申请退款或者支付成功
        if (orderPay.getStatus() != OrderStatus.APPLY_FOR_REFUND.getValue() && orderPay.getStatus() != OrderStatus.PAID_SUCCESS.getValue() ){
            throw new BusinessException("订单状态不对");
        }

        // 3. 创建新的退款信息，通过订单信息和支付信息为退款信息设置属性
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setOrderId(order.getId());
        orderRefund.setRefundMoney(orderPay.getPayMoney());
        orderRefund.setStatus(OrderStatus.REFUND_WAITING_NOTIFY.getValue());
        orderRefundDao.save(orderRefund);
        //保存订单支付结果
        orderPay.setStatus(OrderStatus.REFUND_WAITING_NOTIFY.getValue());
        orderPayDao.save(orderPay);
        //保存订单
        orderDao.save(order);
    }

    @Override
    public String refundCallback(String content) {
        try {
            WxPayRefundNotifyResult notifyResult = this.wxService.parseRefundNotifyResult(content);
            String outTradeNo = notifyResult.getReqInfo().getOutTradeNo();
            Order order = orderDao.findByOutTradeNo(outTradeNo);
            if (order == null) {
                throw new BusinessException("订单不存在");
            }
            if (order.getStatus() != OrderStatus.REFUND_WAITING_NOTIFY.getValue()) {
                throw new BusinessException("订单状态不对");
            }
            order.setStatus(OrderStatus.REFUND_SUCCESS.getValue());
            orderDao.save(order);

            OrderPay orderPay = orderPayDao.findByOrderId(order.getId());
            if (orderPay == null) {
                throw new BusinessException("订单不存在");
            }
            if (orderPay.getStatus() != OrderStatus.REFUND_WAITING_NOTIFY.getValue()) {
                throw new BusinessException("订单状态不对");
            }
            orderPay.setStatus(OrderStatus.REFUND_SUCCESS.getValue());
            orderPayDao.save(orderPay);

            OrderRefund orderRefund = orderRefundDao.findByOrderId(order.getId());
            if (orderRefund == null) {
                throw new BusinessException("订单不存在");
            }
            if (orderRefund.getStatus() != OrderStatus.REFUND_WAITING_NOTIFY.getValue()) {
                throw new BusinessException("订单状态不对");
            }
            orderRefund.setStatus(OrderStatus.REFUND_SUCCESS.getValue());
            orderRefundDao.save(orderRefund);
        } catch (Exception e) {
            throw new BusinessException("微信退款回调失败:", e);
        }
        return "SUCCESS";
    }

    @Override
    public void cancel(Long orderId, Long userId) {
        Order order = orderDao.findById(orderId).orElse(null);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != OrderStatus.READY_FOR_PAY.getValue()) {
            throw new BusinessException("订单状态不对");
        }
        order.setStatus(OrderStatus.CANCELED.getValue());
        orderDao.save(order);
    }

    @Override
    public OrderVO getByOrderId(Long id, Long userId) {
        Order order = orderDao.findById(id).orElse(null);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("该订单不存在");
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setRealPrice(NumberUtils.priceFormatToYuan(order.getRealPrice()));
        orderVO.setTotalPrice(NumberUtils.priceFormatToYuan(order.getTotalPrice()));
        return orderVO;
    }

    @Override
    public Long allOrderNum() {

        return orderDao.count();
    }

    @Override
    public int todaySaleVolume(Long todayZeroTime) {
        return 0;
    }

    @Override
    public void rejectRefund(Long orderId, String reasons) {
        Order order=orderDao.findById(orderId).orElse(null);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != OrderStatus.APPLY_FOR_REFUND.getValue()) {
            throw new BusinessException("订单状态不对");
        }
        order.setStatus(OrderStatus.REFUND_REJECT.getValue());
        //order.setRefundReason(reasons);
        orderDao.save(order);
    }

    /**
     * Rewrite by Eru, 前端也正好方便，就把几个查询合并成一个接口了
     * @param orderId 订单id
     * @param userId 用户id
     * @param page 第几页
     * @param size 每页几条
     * @return 返回对应订单信息
     */
    @Override
    public Page<OrderVO> list(Long orderId, Long userId, int page, int size) {
        Page<Order> result;
        if (orderId == null && userId == null) {
            result = orderDao.findAll(PageRequest.of((page - 1), size));
        } else if (orderId == null) {
            result = orderDao.findAllByUserId(userId, PageRequest.of((page - 1), size));
        } else if (userId == null) {
            result = orderDao.findAllById(orderId, PageRequest.of((page - 1), size));
        } else {
            result = orderDao.findAllByIdAndUserId(orderId, userId, PageRequest.of((page - 1), size));
        }
        List<OrderVO> orderVOS = new ArrayList<>(result.getNumberOfElements());
        result.forEach(item -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(item, orderVO);
            orderVO.setRealPrice(NumberUtils.priceFormatToYuan(item.getRealPrice()));
            orderVO.setTotalPrice(NumberUtils.priceFormatToYuan(item.getTotalPrice()));
            orderVOS.add(orderVO);
        });
        return new PageImpl<>(orderVOS, PageRequest.of((page - 1), size), orderVOS.size());
    }
}
