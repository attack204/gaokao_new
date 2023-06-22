package com.gaokao.common.service.admin;

import com.gaokao.common.meta.vo.admin.PermTreeVO;
import com.gaokao.common.meta.vo.admin.SysPermSaveParams;

import java.util.List;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
public interface SysPermService {
    /**
     * 新增Perm
     */
    Long create(SysPermSaveParams params);

    /**
     * 更新Perm
     * @param id     记录Id
     * @param params 参数信息
     * @return 如果成功返回用户id，否则返回-1
     */
    Long update(Long id, SysPermSaveParams params);

    /**
     * 获取权限树
     */
    List<PermTreeVO> list(Long userId);

    /**
     * 通过id删除
     * @return 返回成功返回编号，否则返回-1
     */
    Long deleteById(Long id);
}
