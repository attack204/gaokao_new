package com.gaokao.common.meta.po;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author MaeYon-Z
 * date 2021-08-22
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_filter_conditions")
public class FilterData {

    @Id
    private Integer id;

    private Integer level;

    private Integer father_id;

    private String label;

}
