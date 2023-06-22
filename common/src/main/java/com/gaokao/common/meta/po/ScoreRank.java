package com.gaokao.common.meta.po;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author MaeYon-Z
 *  date 2021-08-09
 */

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_score_rank")
public class ScoreRank {
    /*
    * 主键
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /*
    * 分数段
    * */
    private int score;

    /*
    * 此段人数
    * */
    private int nums;

    /*
    * 累计人数
    * */
    private int totalNums;

    /*
    * 选考物理的本段人数
    * */
    private int hasPhysicsNums;

    /*
    * 选考物理的累计人数
    * */
    private int hasPhysicsTotal;

    /*
     * 选考化学的本段人数
     * */
    private int hasChemistryNums;

    /*
     * 选考化学的累计人数
     * */
    private int hasChemistryTotal;

    /*
     * 选考生物的本段人数
     * */
    private int hasBiologyNums;

    /*
     * 选考生物的累计人数
     * */
    private int hasBiologyTotal;

    /*
     * 选考政治的本段人数
     * */
    private int hasPoliticsNums;

    /*
     * 选考政治的累计人数
     * */
    private int hasPoliticsTotal;

    /*
     * 选考历史的本段人数
     * */
    private int hasHistoryNums;

    /*
     * 选考历史的累计人数
     * */
    private int hasHistoryTotal;

    /*
     * 选考地理的本段人数
     * */
    private int hasGeographyNums;

    /*
     * 选考地理的累计人数
     * */
    private int hasGeographyTotal;

    public int getTotalNums() {
        return totalNums;
    }
}
