package com.gaokao.common.dao;

import com.gaokao.common.meta.po.UserStar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * @author MaeYon-Z
 * date 2021-09-14
 */

public interface UserStarDao extends PagingAndSortingRepository<UserStar, Long> {

    @Query(value = "select * from tb_user_star where user_id = ?1 ;", nativeQuery = true)
    UserStar getUserStars(Long userId);

}
