package com.gaokao.common.meta.vo.admin;

import lombok.Data;

import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Data
public class UserVO {
    private Long id;

    /**
     * 所属公司、商家
     */
    Long corp;

    /**
     * 商家名称
     */
    private String corpName;
    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;


    /**
     * 状态
     */
    private Integer status;

    private String creator;

    private Long createTime;

    private String updater;

    private Long updateTime;

    private Set<String> roles;

    private Set<Long> roleIds;
}
