package com.gaokao.common.meta.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "tb_sys_perm")
public class SysPerm extends BaseEntity{
    @Id
    private Long id;

    /**
     * 父权限Id
     */
    private Long pid;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限名称
     */
    private String name;
}
