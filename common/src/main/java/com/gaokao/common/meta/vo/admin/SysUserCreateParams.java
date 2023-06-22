package com.gaokao.common.meta.vo.admin;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Data
public class SysUserCreateParams {
    /**
     * 所属公司、商家
     */
    Long corp;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号不合法")
    private String phone;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @Length(min = 8, max = 32, message = "密码长度不合法")
    private String password;

    private Set<Long> roleIds;
}
