package com.gaokao.common.service;

import com.gaokao.common.dao.FilterDataDao;
import com.gaokao.common.meta.po.FilterData;
import com.gaokao.common.meta.vo.filterdata.FilterDataParams;
import com.gaokao.common.meta.vo.filterdata.FilterDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MaeYon-Z
 * date 2021-08-22
 */
@Slf4j
@Service
public class FilterDataServiceImpl implements FilterDataService {

    @Autowired
    private FilterDataDao filterDataDao;


    public List<FilterDataVO> getThreeLayersData(Integer firstLevelId){
        List<FilterDataVO> filterDataVOList = new ArrayList<>();
        List<FilterData> secondLevelDatas = filterDataDao.findSonsByFatherId(firstLevelId);
        secondLevelDatas.forEach(secondLevelData -> {
            FilterDataVO filterDataVO = new FilterDataVO();
            filterDataVO.setValue(secondLevelData.getId());
            filterDataVO.setLabel(secondLevelData.getLabel());
            List<FilterDataParams> filterDataParamsList = new ArrayList<>();
            List<FilterData> thirdlevelDatas = filterDataDao.findSonsByFatherId(secondLevelData.getId());
            thirdlevelDatas.forEach(thirdlevelData -> {
                FilterDataParams filterDataParams = new FilterDataParams();
                filterDataParams.setValue(thirdlevelData.getId());
                filterDataParams.setLabel(thirdlevelData.getLabel());
                filterDataParamsList.add(filterDataParams);
            });
            filterDataVO.setChildren(filterDataParamsList);
            filterDataVOList.add(filterDataVO);
        });
        return filterDataVOList;
    }

    @Override
    public  List<FilterDataParams> getTwoLayersData(Integer firstLevelId){
        List<FilterDataParams> filterDataParamsList = new ArrayList<>();
        List<FilterData> filterDataList = filterDataDao.findSonsByFatherId(firstLevelId);
        filterDataList.forEach(filterData -> {
            FilterDataParams filterDataParams = new FilterDataParams();
            filterDataParams.setValue(filterData.getId());
            filterDataParams.setLabel(filterData.getLabel());
            filterDataParamsList.add(filterDataParams);
        });
        return filterDataParamsList;
    }

}
