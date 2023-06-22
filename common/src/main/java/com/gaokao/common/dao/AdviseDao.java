package com.gaokao.common.dao;

import com.gaokao.common.meta.po.GuessRank;
import com.gaokao.common.meta.po.UserForm;
import com.gaokao.common.meta.po.Volunteer;
import lombok.Data;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author MaeYon-Z
 * date 2021-08-10
 */
@Repository
public interface AdviseDao extends PagingAndSortingRepository<Volunteer, Long> {

    @Query(value = "select * from tb_volunteer where hire_province = '山东'; ", nativeQuery = true)
    List<Volunteer> getAllVolunteer();

    @Query(value = "select * from tb_volunteer where name LIKE ?1 AND professional_name LIKE ?2 ;", nativeQuery = true)
    List<Volunteer> getVolunteerByMajorAndSchool(String schoolParm, String majorParm);

    @Query(value = "select * from tb_volunteer where id = ?1 ; ", nativeQuery = true)
    Volunteer getVolunteer(Long id);
}
