package com.gaokao.common.service;

import com.gaokao.common.dao.SpiderDao;
import com.gaokao.common.meta.po.Spider;
import com.gaokao.common.meta.vo.spider.SpiderMissions;
import com.gaokao.common.utils.SpiderHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author attack204
 * date:  2021/9/24
 * email: 757394026@qq.com
 */
@Slf4j
@Service
public class SpiderServiceImpl implements SpiderService{

    @Autowired
    private SpiderDao spiderDao;

    @Override
    public Long startSpiderMission() {
        try {
            String data = SpiderHttpClientUtil.doGet("http://127.0.0.1:8000/index/");
        } catch (Exception e) {
            return -1L;
        }
        return 1L;
    }

    @Override
    public Page<SpiderMissions> getAllMissions(Integer page, Integer size) {

        List<Spider> spiderList = spiderDao.findAll(PageRequest.of(page - 1, size)).getContent();

        List<SpiderMissions> spiderMissionsList = new ArrayList<>(spiderList.size());

        spiderList.forEach((item) -> {

            SpiderMissions spiderMissions = new SpiderMissions();
            BeanUtils.copyProperties(item, spiderMissions);
            spiderMissionsList.add(spiderMissions);

        });

        return new PageImpl<>(spiderMissionsList, PageRequest.of(page - 1, size), spiderMissionsList.size());
    }
}
