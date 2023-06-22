package com.gaokao.common.service;

import com.gaokao.common.meta.vo.filterdata.FilterDataParams;
import com.gaokao.common.meta.vo.filterdata.FilterDataVO;

import java.util.List;

/**
 * @author MaeYon-Z
 * date 2021-08-22
 */
public interface FilterDataService {

    List<FilterDataVO> getThreeLayersData(Integer firstLevelId);
    List<FilterDataParams> getTwoLayersData(Integer firstLevelId);

}
