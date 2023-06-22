package com.gaokao.common.dao;

import com.gaokao.common.meta.po.Spider;
import com.gaokao.common.meta.po.UserMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author attack204
 * date:  2021/9/27
 * email: 757394026@qq.com
 */
@Repository
public interface SpiderDao extends PagingAndSortingRepository<Spider, Long> {

}
