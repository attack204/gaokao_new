package com.gaokao.common.meta.vo.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MemberCreateParams {

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
