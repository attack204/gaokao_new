package com.gaokao.common.service;

import com.gaokao.common.enums.VeryCodeType;

public interface VerifyCodeService {
    /**
     * 发送验证码
     *
     * @param type 验证码类型
     * @param res  资源，比如手机号码
     * @return 是否发送成功
     */
    String sendVeryCode(VeryCodeType type, String res);

    /**
     * 验证验证码是否合法
     *
     * @param type    验证码类型
     * @param res     资源类型
     * @param srcCode 需要检验的验证码
     * @return 是否校验通过
     */
    boolean verify(VeryCodeType type, String res, String srcCode);
}
