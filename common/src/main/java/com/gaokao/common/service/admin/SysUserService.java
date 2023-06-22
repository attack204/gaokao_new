package com.gaokao.common.service.admin;

import com.gaokao.common.meta.bo.UserInfo;
import com.gaokao.common.meta.po.SysUser;
import com.gaokao.common.meta.vo.admin.SysUserCreateParams;
import com.gaokao.common.meta.vo.admin.SysUserUpdateParams;
import com.gaokao.common.meta.vo.admin.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */

public interface SysUserService extends UserDetailsService {

    /**
     * 添加管理员
     *
     * @param params 注册参数
     * @return 注册成功返回用户Id
     */
    Long create(SysUserCreateParams params);

    /**
     * 给指定的用户分配角色
     *
     * @param userId  用户Id
     * @param roleIds 角色id
     * @return 是否分配成功
     */
    boolean allocRoles(Long userId, Set<Long> roleIds);

    /**
     * 获取用户详细信息
     *
     * @param userId 用户Id
     * @return 用户详细信息
     */
    UserInfo getInfo(Long userId);


    SysUser getByUsername(String username);

    /**
     * 分页获取用户列表
     */
    Page<UserVO> list(String keyword, Integer page, Integer size, Long corpId);

    /**
     * 修改用户名密码
     */
    int changePwd(long id, String originPwd, String newPwd);

    /**
     * 删除指定id的用户
     */
    Long deleteById(Long id);

    /**
     * 更新指定id的用户
     */
    Long updateById(SysUserUpdateParams params, Long id);

    /**
     * 根据id获取用户
     */
    SysUser getById(Long id);
}
