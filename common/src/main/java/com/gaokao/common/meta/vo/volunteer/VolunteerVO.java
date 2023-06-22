package com.gaokao.common.meta.vo.volunteer;

import lombok.Data;

import java.util.List;

/**
 * @author attack204
 * date:  2021/8/18
 * email: 757394026@qq.com
 */
@Data
public class VolunteerVO {

    private Long id;

    private Integer volunteerPosition; // 第几志愿

    private String name;

    private String universityCode;

    private String province;

    private String city;

    private String address;

    private Boolean undergraduateSchoolIsOrNot;

    private Boolean juniorSchoolIsOrNot;

    private Boolean is985;

    private Boolean is211;

    private Boolean PublicIsOrNot;

    private Boolean PrivateIsOrNot;

    private String category;

    private String picLink;

    private Integer employmentRate;

    private Integer abroadRate;

    private Integer furtherRate;

    private Boolean volunteerSection;

    private Integer lowestScore;

    private Integer lowestPosition;

    private String professionalName;

    private Integer subjectRestrictionType;

    private List<Integer> subjectRestrictionDetail;

    private String majorCode;

    private Integer score;

    private Integer position;

    private Integer enrollment;

    private Integer time;

    private Integer fee;

    private Integer doubleFirstClassSubjectNumber;

    private Integer countrySpecificSubjectNumber;

    private Integer masterPoint;

    private Integer doctorPoint;

    private Boolean myStar;
}