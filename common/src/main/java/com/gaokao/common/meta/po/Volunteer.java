package com.gaokao.common.meta.po;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author attack204
 * date:  2021/8/18
 * email: 757394026@qq.com
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_volunteer")
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String universityCode;

    private String province;

    private String city;

    private String address;

    @Column(name = "is_undergraduate_school")
    private Boolean undergraduateSchoolIsOrNot;

    @Column(name = "is_junior_school")
    private Boolean juniorSchoolIsOrNot;

    @Column(name = "is_985")
    private Boolean is985;

    @Column(name = "is_211")
    private Boolean is211;

    @Column(name = "is_public")
    private Boolean PublicIsOrNot;

    @Column(name = "is_private")
    private Boolean PrivateIsOrNot;

    private String category;

    private String picLink;

    private Integer employmentRate;

    private Integer abroadRate;

    private Integer furtherRate;

    private Integer volunteerSection;

    private Integer lowestScore;

    private Integer lowestPosition;

    private String professionalName;

    private Integer subjectRestrictionType;

    private String subjectRestrictionDetail;

    private Integer score;

    private Integer position;

    private Integer enrollment;

    private Integer time;

    private Integer fee;

    private Integer doubleFirstClassSubjectNumber;

    private Integer countrySpecificSubjectNumber;

    private Integer masterPoint;

    private Integer doctorPoint;
}
