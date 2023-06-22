package com.gaokao.common.service.admin;

import com.alibaba.fastjson.JSON;
import com.gaokao.common.dao.AdviseDao;
import com.gaokao.common.dao.UserMemberDao;
import com.gaokao.common.dao.UserStarDao;
import com.gaokao.common.meta.po.UserMember;
import com.gaokao.common.meta.po.UserStar;
import com.gaokao.common.meta.po.Volunteer;
import com.gaokao.common.meta.vo.advise.AdviseVO;
import com.gaokao.common.meta.vo.volunteer.VolunteerVO;
import com.gaokao.common.service.UserStarService;
import com.gaokao.common.utils.UserUtils;
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
 * @author MaeYon-Z
 * @create 2021-09-14
 */
@Slf4j
@Service
public class UserStarServiceImpl implements UserStarService {

    @Autowired
    private UserMemberDao userMemberDao;

    @Autowired
    private UserStarDao userStarDao;

    @Autowired
    private AdviseDao adviseDao;

    private Integer getRate(Integer rank, Integer guess){
        Integer dif = Math.abs(rank - guess);
        Integer rate = 0;
        if(rank > guess){//用户排名大于预估最低录取名次
            if(dif < 1000)
                rate = 60 - dif/100;
            else
                rate = 50 - (dif-1000)/50;
            if (rate < 10)
                rate = 10;
        }
        else if(rank <= guess){//用户排名小于预估最低录取名次
            if(dif < 1000)
                rate = 60 + dif/100;
            else
                rate = 70 + (dif-1000)/50;
            if(rate > 95)
                rate = 95;
        }
        return rate;
    }

    @Override
    public Page<AdviseVO> getUserStarList(Integer page, Integer size){
        Long userId = UserUtils.getUserId();
        UserMember userMember = userMemberDao.findUserMemberById(userId);
        UserStar userStar = userStarDao.getUserStars(userId);
        List<AdviseVO> adviseVOList = new ArrayList<>();
        if(userStar == null){
            return new PageImpl<>(adviseVOList, PageRequest.of(page - 1, size), adviseVOList.size());
        }
        String userStars = userStar.getStars();
        List<Long> userStarList = JSON.parseArray(userStars, Long.class);
        userStarList.forEach(id -> {
            Volunteer volunteer = adviseDao.getVolunteer(id);
            AdviseVO adviseVO = new AdviseVO();
            VolunteerVO volunteerVO = new VolunteerVO();
            BeanUtils.copyProperties(volunteer, volunteerVO);
            if(volunteer.getVolunteerSection() == 1){
                volunteerVO.setVolunteerSection(true);
            }
            else{
                volunteerVO.setVolunteerSection(false);
            }
            volunteerVO.setSubjectRestrictionDetail(JSON.parseArray(volunteer.getSubjectRestrictionDetail(), Integer.class));
            volunteerVO.setMyStar(true);
            Integer rate = getRate(userMember.getProvinceRank().intValue(), volunteerVO.getLowestPosition());
            String rateDesc = "";
            if(rate <= 50)
                rateDesc = "难录取";
            else if(rate > 50 && rate <= 60)
                rateDesc = "可冲击";
            else if(rate > 60 && rate <= 80)
                rateDesc = "较稳妥";
            else if(rate > 80 && rate < 95)
                rateDesc = "可保底";
            else if(rate == 95)
                rateDesc = "浪费分";
            adviseVO.setRate(rate);
            adviseVO.setVolunteerVO(volunteerVO);
            adviseVO.setRateDesc(rateDesc);
            adviseVOList.add(adviseVO);

        });
        Integer fromIndex = (page - 1)*size;
        Integer toIndex = fromIndex + size;
        if(toIndex >= adviseVOList.size()){
            toIndex = adviseVOList.size();
        }
        List<AdviseVO> adviseVOS = adviseVOList.subList(fromIndex, toIndex);
        return new PageImpl<>(adviseVOS, PageRequest.of(page - 1, size), adviseVOList.size());
    }

    @Override
    public Boolean star(Long volunteerId){
        Long userId = UserUtils.getUserId();
        UserStar userStar = userStarDao.getUserStars(userId);
        if(userStar == null){
            UserStar userStar1 = new UserStar();
            userStar1.setUserId(userId);
            userStar1.setStars("[" + volunteerId + "]");
            userStarDao.save(userStar1);
            return true;
        }
        String stars = userStar.getStars();
        List<Long> userStarList = JSON.parseArray(stars, Long.class);
        if(userStarList.contains(volunteerId)){
            userStarList.remove(volunteerId);
            stars = JSON.toJSONString(userStarList);
            UserStar userStar1 = new UserStar();
            userStar1.setId(userStar.getId());
            userStar1.setUserId(userStar.getUserId());
            userStar1.setStars(stars);
            userStarDao.save(userStar1);
            return true;
        }else {
            userStarList.add(volunteerId);
            stars = JSON.toJSONString(userStarList);
            UserStar userStar1 = new UserStar();
            userStar1.setId(userStar.getId());
            userStar1.setUserId(userStar.getUserId());
            userStar1.setStars(stars);
            userStarDao.save(userStar1);
            return true;
        }
    }
}
