package com.gaokao.common.service.admin;

import com.gaokao.common.meta.vo.admin.SysRoleSaveParams;
import com.gaokao.common.meta.vo.admin.SysRoleVO;
import com.gaokao.common.meta.vo.common.NameValuePair;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
public interface SysRoleService {

    /**
     * 保存角色
     */
    Long insert(SysRoleSaveParams params, Long corpId, String operator);

    /**
     * 更新指定角色
     */
    Long update(Long id, SysRoleSaveParams params);

    /**
     * 删除指定用户
     *
     * @param id 用户id
     * @return 异常返回-1，否则返回用户id
     */
    Long delete(Long id);

    /**
     * 分页获取角色列表
     */
    Page<SysRoleVO> list(String keyword, Long corpId, Long operatorCorpId, Integer page, Integer size);

    /**
     * 获取指定角色的所有权限Id
     *
     * @param roleIds 角色Id
     * @return 权限集合
     */
    Set<String> listAllPermsByRoleIds(Set<Long> roleIds);

    /**
     * 获取指定公司下的角色
     */
    List<NameValuePair> list(Long corpId);
}
