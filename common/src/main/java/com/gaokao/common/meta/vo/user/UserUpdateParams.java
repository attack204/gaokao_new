package com.gaokao.common.meta.vo.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author attack204
 * date:  2021/7/20
 * email: 757394026@qq.com
 */
@Data
public class UserUpdateParams {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能空")
    private String phone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能空")
    private String password;

    /**
     * 昵称
     */
    private String nickname;
}
