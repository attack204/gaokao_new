package com.gaokao.common.meta.vo.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermTreeVO extends SysPermSaveParams {
    private List<PermTreeVO> children;
}
