package com.gaokao.common.meta.vo.admin;

import lombok.Data;

import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/20
 * email: 757394026@qq.com
 */
@Data
public class SysRoleSaveParams {
    /**
     * 名称
     */
    private String name;

    /**
     * 权限Id
     */
    private Set<Long> permIds;
}
