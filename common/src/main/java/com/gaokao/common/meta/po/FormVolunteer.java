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
@Table(name = "tb_form_volunteer")
public class FormVolunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long formId;

    private Boolean volunteerSection;

    private Integer volunteerPosition;

    private Long volunteerId;
}
