package com.gaokao.common.dao;

import com.gaokao.common.meta.po.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Repository
public interface SysUserDao extends PagingAndSortingRepository<SysUser, Long> {
    SysUser findSysUserByUsername(String username);

    SysUser findByPhone(String phone);

    Page<SysUser> findByUsernameContaining(String username, Pageable pageable);

    Page<SysUser> findByUsernameContainingAndAndCorpAfter(String username, Long corpId, Pageable pageable);

    List<SysUser> findAllById(Long id);

    SysUser findSysUserById(Long id);
}
