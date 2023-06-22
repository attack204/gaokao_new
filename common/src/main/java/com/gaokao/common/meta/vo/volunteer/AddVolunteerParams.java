package com.gaokao.common.meta.vo.volunteer;

import lombok.Data;

/**
 * @author attack204
 * date:  2021/8/7
 * email: 757394026@qq.com
 */
@Data
public class AddVolunteerParams {

    private Long formId;

    private Boolean section;

    /**
     * 第几志愿
     */
    private Integer volunteerPosition;

    /**
     * 志愿id
     */
    private Long volunteerId;

}
