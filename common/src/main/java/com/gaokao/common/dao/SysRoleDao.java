package com.gaokao.common.dao;

import com.gaokao.common.meta.po.SysRole;
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
public interface SysRoleDao extends PagingAndSortingRepository<SysRole, Long> {

    Page<SysRole> findAllByNameContainingAndCorp(String name,Long corp, Pageable pageable);

    Page<SysRole> findAllByNameContaining(String name, Pageable pageable);

    List<SysRole> findAllByCorp(Long corp);

    SysRole findAllByName(String name);
}
