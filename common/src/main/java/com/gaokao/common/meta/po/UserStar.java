package com.gaokao.common.meta.po;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author MaeYon-Z
 * @create 2021-09-14
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_user_star")
public class UserStar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String stars;
}
