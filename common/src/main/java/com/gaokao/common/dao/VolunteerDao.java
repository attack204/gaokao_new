package com.gaokao.common.dao;

import com.gaokao.common.meta.po.Volunteer;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author attack204
 * date:  2021/8/18
 * email: 757394026@qq.com
 */
public interface VolunteerDao extends PagingAndSortingRepository<Volunteer, Long> {
}
