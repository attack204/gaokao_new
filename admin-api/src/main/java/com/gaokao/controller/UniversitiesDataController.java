package com.gaokao.controller;

import com.gaokao.common.meta.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author attack204
 * date:  2021/7/20
 * email: 757394026@qq.com
 * desc: 用于高校数据管理，包括数据导入，以及对应item的crud
 */
@Slf4j
@RestController
@RequestMapping("/xhr/v1/university")
public class UniversitiesDataController {

    /**
     * 调用远程服务，爬取高校信息
     * @return
     */
    @GetMapping("/fetch")
    public AjaxResult<Long> fetchRemoteData() {
        return null;
    }

    /**
     * 调用远程服务，更新数据库信息
     * @return
     */
    @GetMapping("/update")
    public AjaxResult<Long> updateDatabase() {
        return null;
    }



}
