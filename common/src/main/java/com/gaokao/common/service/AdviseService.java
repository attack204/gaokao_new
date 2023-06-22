package com.gaokao.common.service;

import com.gaokao.common.dao.AdviseDao;
import com.gaokao.common.meta.po.Volunteer;
import com.gaokao.common.meta.vo.advise.AutoGenerateFormParams;
import com.gaokao.common.meta.vo.advise.FilterParams;
import com.gaokao.common.meta.vo.advise.AdviseVO;
import com.gaokao.common.meta.vo.volunteer.UserFormDetailVO;
import com.gaokao.common.meta.vo.volunteer.VolunteerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author MaeYon-Z
 *  date 2021-08-09
 */
public interface AdviseService {

    //根据分数获得用户排名
    Integer getUserRank(Integer score);

    /*
    * 按照type分类输出，type取值为：
    *   0：全部
    *   1：可冲击
    *   2：较稳妥
    *   3：可保底
    * */
    Page<AdviseVO> list(FilterParams filterParams);

    //生成志愿表
    UserFormDetailVO generateVoluntForm(AutoGenerateFormParams autoGenerateFormParams);


}
