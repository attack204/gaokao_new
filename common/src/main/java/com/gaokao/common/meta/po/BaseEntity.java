package com.gaokao.common.meta.po;

import lombok.Data;

import javax.persistence.MappedSuperclass;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Data
@MappedSuperclass
public class BaseEntity {

    private String creator;

    private Long createTime;

    private String updater;

    private Long updateTime;
}
