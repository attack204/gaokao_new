package com.gaokao.common.service;

import com.gaokao.common.meta.po.FormVolunteer;
import com.gaokao.common.meta.vo.volunteer.UserFormAllVO;
import com.gaokao.common.meta.vo.volunteer.UserFormDetailVO;

import java.util.List;

/**
 * @author attack204
 * date:  2021/8/8
 * email: 757394026@qq.com
 */
public interface VolunteerService {

    Long create(Long userId, List<Long> subject, Integer score, Boolean generatedType, String name);

    Long setVolunteer(Long userId, Long formId, Boolean section,Integer position, Long volunteerId);

    Long updateUserFormName(Long userId, Long formId, String newName);

    List<UserFormAllVO> listAll(Long userId);

    UserFormDetailVO listCurrentForm(Long userId);

    Long changeCurrentForm(Long userId, Long newFormId);

    Long deleteForm(Long formId);

    FormVolunteer findByFormIdAndSectionAndVolunteerPosition(Long formId, Boolean section, Integer position);

    Boolean queryExist(Long formId, Boolean volunteerSection, Integer position);

}
