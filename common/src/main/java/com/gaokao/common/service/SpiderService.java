package com.gaokao.common.service;

import com.gaokao.common.meta.vo.spider.SpiderMissions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author attack204
 * date:  2021/9/24
 * email: 757394026@qq.com
 */
public interface SpiderService {

    Long startSpiderMission();

    Page<SpiderMissions> getAllMissions(Integer page, Integer size);

}
