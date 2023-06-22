package com.gaokao.common.meta.vo.admin;

import lombok.Data;

import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/20
 * email: 757394026@qq.com
 */
@Data
public class SysRoleVO {
    private Long id;

    /**
     * 所属公司、商家
     */
    Long corp;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Integer status;

    private String creator;

    private Long createTime;

    private String updater;

    private Long updateTime;

    private Set<Long> permIds;
}
