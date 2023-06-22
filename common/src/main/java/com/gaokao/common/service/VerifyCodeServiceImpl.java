package com.gaokao.common.service;

import cn.hutool.core.util.RandomUtil;
import com.gaokao.common.enums.VeryCodeType;
import com.gaokao.common.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String sendVeryCode(VeryCodeType type, String res) {
        //TODO: 频率校验等
        String code = RandomUtils.randomNum(type.getLength());
        redisTemplate.opsForValue().set(String.join("_", type.getType(), res), code, type.getTtl(), TimeUnit.SECONDS);
        //TODO:发送验证码，暂时先发送给前端，后续发送短信成功后，需要调整
        //打印出来
        log.info("[sendVeryCode] type={} res={} code={}", type, res, code);
        return code;
    }

    @Override
    public boolean verify(VeryCodeType type, String res, String srcCode) {
        String result = redisTemplate.opsForValue().get(String.join("_", type.getType(), res));
        return srcCode.equals(result);
    }

}
