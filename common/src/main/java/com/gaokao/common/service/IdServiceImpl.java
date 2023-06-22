package com.gaokao.common.service;

import com.gaokao.common.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Slf4j
@Service
public class IdServiceImpl implements IdService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Long genOrderId(Long userId) {
        if (userId > 100000000) {
            //TODO: 用户Id暂不支持大于100000000
            throw new BusinessException("用户Id非法");
        }

        //一共19位：8位年月日 8位用户 3位自增订单
        Calendar now = Calendar.getInstance();
        Integer year = now.get(Calendar.YEAR);
        Integer month = now.get(Calendar.MONTH) + 1; //第一个月从0开始，所以得到月份＋1
        Integer day = now.get(Calendar.DAY_OF_MONTH);

        StringBuilder idBuilder = new StringBuilder();
        idBuilder.append(String.format("%04d", year));
        idBuilder.append(String.format("%02d", month));
        idBuilder.append(String.format("%02d", day));
        idBuilder.append(String.format("%08d", userId));

        String key = idBuilder.toString();

        Long orderCount = redisTemplate.opsForValue().increment(key);
        if (orderCount == null || orderCount > 999) {
            //TODO: 每用户每日订单个数不能超过999
            throw new BusinessException("订单个数大于999");
        }
        idBuilder.append(String.format("%03d", orderCount));

        return Long.valueOf(idBuilder.toString());
    }

    @Override
    public Long genOrderPayId(Long orderId) {
        //一共19位：8位年月日 11位订单id
        Calendar now = Calendar.getInstance();
        Integer year = now.get(Calendar.YEAR);
        Integer month = now.get(Calendar.MONTH) + 1; //第一个月从0开始，所以得到月份＋1
        Integer day = now.get(Calendar.DAY_OF_MONTH);

        StringBuilder idBuilder = new StringBuilder();
        idBuilder.append(String.format("%04d", year));
        idBuilder.append(String.format("%02d", month));
        idBuilder.append(String.format("%02d", day));

        String key = idBuilder.toString();

        Long orderCount = redisTemplate.opsForValue().increment(key);
        idBuilder.append(String.format("%011d", orderCount));

        return Long.valueOf(idBuilder.toString());
    }
}
