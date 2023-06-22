package com.gaokao.common.meta.vo.admin;

import lombok.Data;

import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/20
 * email: 757394026@qq.com
 */
@Data
public class AllocUserRoles {
    private Long userId;
    private Set<Long> roleIds;
}
