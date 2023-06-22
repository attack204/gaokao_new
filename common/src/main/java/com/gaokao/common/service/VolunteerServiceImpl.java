package com.gaokao.common.service;

import com.alibaba.fastjson.JSON;
import com.gaokao.common.constants.VolunteerConstant;
import com.gaokao.common.dao.FormVolunteerDao;
import com.gaokao.common.dao.VolunteerDao;
import com.gaokao.common.dao.UserFormDao;
import com.gaokao.common.enums.Subject;
import com.gaokao.common.meta.po.FormVolunteer;
import com.gaokao.common.meta.po.UserForm;
import com.gaokao.common.meta.po.Volunteer;
import com.gaokao.common.meta.vo.volunteer.UserFormAllVO;
import com.gaokao.common.meta.vo.volunteer.UserFormDetailVO;
import com.gaokao.common.meta.vo.volunteer.VolunteerCreateParams;
import com.gaokao.common.meta.vo.volunteer.VolunteerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author attack204
 * date:  2021/8/8
 * email: 757394026@qq.com
 */
@Slf4j
@Service
public class VolunteerServiceImpl implements VolunteerService{

    @Autowired
    private UserFormDao userFormDao;

    @Autowired
    private FormVolunteerDao formVolunteerDao;

    @Autowired
    private VolunteerDao volunteerDao;

    @Override
    public Long create(Long userId, List<Long> subject, Integer score, Boolean generatedType, String name) {
        UserForm userForm = new UserForm();

        if(userFormDao.findAllByUserId(userId).size() == 0) {
            userForm.setCurrent(true);
        } else {
            userForm.setCurrent(false);
        }
        userForm.setName(name);
        userForm.setGeneratedType(generatedType);
        userForm.setUserId(userId);
        userForm.setSubject(JSON.toJSONString(subject));
        userForm.setScore(score);
        userForm.setGeneratedTime(System.currentTimeMillis());
        return userFormDao.save(userForm).getId();
    }

    @Override
    public Boolean queryExist(Long formId, Boolean volunteerSection, Integer position) {

        if(formVolunteerDao.findByFormIdAndVolunteerSectionAndVolunteerPosition(formId, volunteerSection, position) != null) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Long changeCurrentForm(Long userId, Long newFormId) {

        UserForm userForm = userFormDao.findUserFormByUserIdAndCurrent(userId, Boolean.TRUE);
        if(userForm != null ) {
            userForm.setCurrent(false);
            userFormDao.save(userForm);
        }

        UserForm newUserForm = userFormDao.findById(newFormId).orElse(null);
        newUserForm.setCurrent(true);
        return userFormDao.save(newUserForm).getId();

    }

    @Override
    public Long deleteForm(Long formId) {

        userFormDao.deleteById(formId);
        return formVolunteerDao.deleteAllByFormId(formId);



    }

    @Override
    public Long setVolunteer(Long userId, Long formId, Boolean section, Integer position, Long volunteerId) {
        if(section == null) {
            section = new Boolean(Boolean.TRUE);
        }
        FormVolunteer formVolunteer = formVolunteerDao.findByFormIdAndVolunteerSectionAndVolunteerPosition(formId, section, position);
        FormVolunteer formVolunteerRepeatCheck = formVolunteerDao.findByFormIdAndVolunteerSectionAndVolunteerId(formId, section, volunteerId);
        if(formVolunteerRepeatCheck != null && formVolunteerRepeatCheck.getVolunteerPosition() != position) {
            return -1L;
        }
        if(formVolunteer == null) {
            if(volunteerId == VolunteerConstant.EMPTY_VOLUNTEER) {
                return 0L;
            }
            FormVolunteer newFormVolunteer = new FormVolunteer();
            newFormVolunteer.setFormId(formId);
            newFormVolunteer.setVolunteerSection(section);
            newFormVolunteer.setVolunteerPosition(position);
            newFormVolunteer.setVolunteerId(volunteerId);
            return formVolunteerDao.save(newFormVolunteer).getId();
        } else {
            if(volunteerId == VolunteerConstant.EMPTY_VOLUNTEER) {
                formVolunteerDao.deleteById(formVolunteer.getId());
                return formVolunteer.getId();
            }
            formVolunteer.setVolunteerId(volunteerId);
            return formVolunteerDao.save(formVolunteer).getId();
        }

    }

    @Override
    public Long updateUserFormName(Long userId, Long formId, String newName) {

        UserForm UserForm = userFormDao.findById(formId).orElse(null);

        if(UserForm == null) {
            return -1L;
        }

        UserForm.setName(newName);

        return userFormDao.save(UserForm).getId();
    }

    public List<Long> convertSubjectInformation(String subject) {

        List<Long> list = JSON.parseArray(subject, Long.class);

        return list;

    }

    public UserFormAllVO convertToUserFormAllVO(UserForm userForm) {

        UserFormAllVO userFormAllVO = new UserFormAllVO();
        BeanUtils.copyProperties(userForm, userFormAllVO);

        List<Long> subjectList = convertSubjectInformation(userForm.getSubject());

        userFormAllVO.setSubject(subjectList);

        return userFormAllVO;


    }

    /**
     * 返回所有表格的基础信息
     * @param userId
     * @return
     */
    @Override
    public List<UserFormAllVO> listAll(Long userId) {

        List<UserForm> userForms = userFormDao.findAllByUserId(userId);

        List<UserFormAllVO> userFormAllVOS = new ArrayList<>(userForms.size());

        userForms.forEach((item) -> {
            userFormAllVOS.add(convertToUserFormAllVO(item));
        });

        return userFormAllVOS;
    }



    /**
     * 返回当前志愿的详细信息
     * @param userId
     * @return
     */

    @Override
    public UserFormDetailVO listCurrentForm(Long userId) {

        UserForm userForm = userFormDao.findUserFormByUserIdAndCurrent(userId, Boolean.TRUE);
        if(userForm == null) {
            return null;
        }
        UserFormDetailVO userFormDetailVO = new UserFormDetailVO();
        BeanUtils.copyProperties(userForm, userFormDetailVO);
        userFormDetailVO.setSubject(convertSubjectInformation(userForm.getSubject()));

        List<FormVolunteer> volunteerList = formVolunteerDao.findAllByFormId(userForm.getId());
        List<VolunteerVO> volunteerVOS = new ArrayList<>(volunteerList.size());
        volunteerList.forEach((item) ->  {
            VolunteerVO volunteerVO = new VolunteerVO();
            Volunteer volunteer = volunteerDao.findById(item.getVolunteerId()).orElse(null);
            BeanUtils.copyProperties(volunteer, volunteerVO);
            //volunteerVO.setCategory(JSON.parseArray(volunteer.getCategory(), String.class));
            volunteerVO.setSubjectRestrictionDetail(JSON.parseArray(volunteer.getSubjectRestrictionDetail(), Integer.class));
            volunteerVO.setVolunteerPosition(item.getVolunteerPosition());
            volunteerVO.setVolunteerSection(item.getVolunteerSection());

            volunteerVOS.add(volunteerVO);
        });
        userFormDetailVO.setVolunteerList(volunteerVOS);
        return userFormDetailVO;

    }

    @Override
    public FormVolunteer findByFormIdAndSectionAndVolunteerPosition(Long formId, Boolean section, Integer position) {
        return formVolunteerDao.findByFormIdAndVolunteerSectionAndVolunteerPosition(formId, section, position);

    }
}
