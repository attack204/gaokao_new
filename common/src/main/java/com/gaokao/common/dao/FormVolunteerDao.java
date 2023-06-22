package com.gaokao.common.dao;

import com.gaokao.common.meta.po.FormVolunteer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author attack204
 * date:  2021/8/18
 * email: 757394026@qq.com
 */
@Repository
public interface FormVolunteerDao extends PagingAndSortingRepository<FormVolunteer, Long> {

    FormVolunteer findByFormIdAndVolunteerSectionAndVolunteerPosition(Long formId, Boolean volunteerSection, Integer volunteerPosition);

    FormVolunteer findByFormIdAndVolunteerSectionAndVolunteerId(Long formId, Boolean volunteerSection, Long volunteerId);

    List<FormVolunteer> findAllByFormId(Long formId);

    @Transactional
    Long deleteAllByFormId(Long formId);

}
