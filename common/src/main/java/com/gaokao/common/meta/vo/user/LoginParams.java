package com.gaokao.common.meta.vo.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginParams extends RegParams {
    private String username;
}
