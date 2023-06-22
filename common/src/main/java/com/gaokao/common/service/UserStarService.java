package com.gaokao.common.service;

import com.gaokao.common.meta.vo.advise.AdviseVO;
import com.gaokao.common.meta.vo.volunteer.VolunteerVO;
import org.springframework.data.domain.Page;

/**
 * @author MaeYon-Z
 * date 2021-09-14
 */
public interface UserStarService {

    Page<AdviseVO> getUserStarList(Integer page, Integer size);

    Boolean star(Long volunteerId);

}
