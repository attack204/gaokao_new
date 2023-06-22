package com.gaokao.common.dao;

import com.gaokao.common.meta.po.UserForm;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author attack204
 * date:  2021/8/8
 * email: 757394026@qq.com
 */
@Repository
public interface UserFormDao extends PagingAndSortingRepository<UserForm, Long> {

    List<UserForm> findAllByUserId(Long userId);

    @Query(value = "select * from tb_user_form where user_id = ?1 and is_current = ?2 limit 1;", nativeQuery = true)
    UserForm findUserFormByUserIdAndCurrent(Long userId, Boolean current);

    @Query(value = "select * from tb_user_form where user_id = ?1 and generated_time = ?2 ;", nativeQuery = true)
    UserForm findForm(Long userId, Long time);

}
