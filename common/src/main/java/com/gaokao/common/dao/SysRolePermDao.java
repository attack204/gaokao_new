package com.gaokao.common.dao;

import com.gaokao.common.meta.po.SysRolePerm;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Repository
public interface SysRolePermDao extends PagingAndSortingRepository<SysRolePerm, Long> {
    @Transactional
    Long deleteAllByRoleId(Long roleId);

    List<SysRolePerm> findAllByRoleId(Long roleId);
}
