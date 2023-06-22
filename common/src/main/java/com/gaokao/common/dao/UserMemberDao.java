package com.gaokao.common.dao;


import com.gaokao.common.meta.po.SysUserRole;
import com.gaokao.common.meta.po.UserMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMemberDao extends PagingAndSortingRepository<UserMember, Long> {

    UserMember findUserMemberByPhone(String phone);

    //List<UserMember> findAllByUserId(Long userId);

    UserMember findUserMemberById(Long userId);

    List<UserMember> findByUsernameContaining(String keyword, Pageable pageable);

    void deleteById(Long userId);

   // Long deleteAllByUserId(Long userId);

    UserMember findByWxOpenId(String s);

    UserMember findByUsernameContaining(String s);

    UserMember findByUsername(String s);

   //  对手机号进行搜索
    UserMember findByPhone(String s);

    List<UserMember> findByPhoneContaining(String keyword, Pageable pageable);


}
