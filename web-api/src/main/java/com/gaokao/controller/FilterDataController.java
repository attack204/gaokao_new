package com.gaokao.controller;

import com.gaokao.common.meta.AjaxResult;
import com.gaokao.common.meta.vo.filterdata.FilterDataParams;
import com.gaokao.common.meta.vo.filterdata.FilterDataVO;
import com.gaokao.common.service.FilterDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MaeYon-Z
 * date 2021-08-22
 */
@Slf4j
@RestController
@RequestMapping("/xhr/v1/getData")
public class FilterDataController {

    @Autowired
    private FilterDataService filterDataService;

    @GetMapping("/batch")
    public AjaxResult<List<FilterDataParams>> getBatch(){
        return AjaxResult.SUCCESS(filterDataService.getTwoLayersData(1));
    }

    @GetMapping("/region")
    public AjaxResult<List<FilterDataVO>> getRegion(){
        return AjaxResult.SUCCESS(filterDataService.getThreeLayersData(2));
    }

    @GetMapping("/schooltype")
    public AjaxResult<List<FilterDataVO>> getSchoolType(){
        return AjaxResult.SUCCESS(filterDataService.getThreeLayersData(3));
    }

    @GetMapping("/majortype")
    public AjaxResult<List<FilterDataVO>> getMajorType(){
        return AjaxResult.SUCCESS(filterDataService.getThreeLayersData(4));
    }

}
