package com.gaokao.common.meta.bo;

import lombok.Data;

import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Data
public class UserInfo {

    private Long corp;

    private String name;

    private Set<String> roles;

    private Set<String> permissions;
}
