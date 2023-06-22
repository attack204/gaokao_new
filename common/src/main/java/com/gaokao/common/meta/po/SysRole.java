package com.gaokao.common.meta.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_sys_role")
public class SysRole extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属公司、商家
     */
    Long corp;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Integer status;
}
