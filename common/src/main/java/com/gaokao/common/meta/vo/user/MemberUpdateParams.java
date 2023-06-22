package com.gaokao.common.meta.vo.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MemberUpdateParams {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能空")
    private String phone;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能空")
    private String username;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 全省排名
     */
    private Long provinceRank;
    /**
     * 选考科目
     */
    private String subject;


}
