package com.gaokao.common.meta.po;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author MaeYon-Z
 * date 2021-08-10
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_guess_rank")
public class GuessRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    * 大学id
    * */
    private Long universityId;

    /*
    * 专业代码
    * */
    private String majorCode;

    /*
    * 预测最低名次
    * */
    private int guessRank;

}
