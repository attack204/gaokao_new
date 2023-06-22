package com.gaokao.common.dao;

import com.gaokao.common.meta.po.FilterData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author MaeYon-Z
 * date 2021-08-22
 */
public interface FilterDataDao extends PagingAndSortingRepository<FilterData, Integer> {

    @Query(value = "select * from tb_filter_conditions where father_id = ?;", nativeQuery = true)
    List<FilterData> findSonsByFatherId(Integer fatherId);

    @Query(value = "select * from tb_filter_conditions where id = ? ;", nativeQuery = true)
    FilterData getOneById(Integer id);

}
