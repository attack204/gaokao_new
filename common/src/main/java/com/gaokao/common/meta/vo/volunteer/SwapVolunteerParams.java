package com.gaokao.common.meta.vo.volunteer;

import lombok.Data;

/**
 * @author attack204
 * date:  2021/8/7
 * email: 757394026@qq.com
 */
@Data
public class SwapVolunteerParams {

    private Long formId;

    /**
     * 是在哪一段进行交换
     */

    private Boolean section;

    /**
     * 需要交换的志愿的位置
     */
    private Integer firstVolunteerPosition;

    /**
     * 需要交换的志愿的id
     */
    private Long firstVolunteerId;


    /**
     * 目标志愿的位置
     */
    private Integer secondVolunteerPosition;

    /**
     * 目标志愿的id
     */
    private Long secondVolunteerId;

}
