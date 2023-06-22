package com.gaokao.common.meta.vo.admin;

import lombok.Data;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Data
public class SysPermSaveParams {
    /**
     * 记录Id
     */
    private Long id;

    /**
     * 父记录Id
     */
    private Long pid;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限名称
     */
    private String name;
}
