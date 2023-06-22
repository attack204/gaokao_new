package com.gaokao.common.meta.po;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author attack204
 * date:  2021/8/8
 * email: 757394026@qq.com
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_user_form")
public class UserForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String name;

    private Integer score;

    private String subject;

    private Boolean generatedType;

    @Column(name = "is_current")
    private Boolean current;

    private Long generatedTime;

}
