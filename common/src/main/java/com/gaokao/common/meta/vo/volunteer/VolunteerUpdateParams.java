package com.gaokao.common.meta.vo.volunteer;

import lombok.Data;

/**
 * @author attack204
 * date:  2021/8/7
 * email: 757394026@qq.com
 */
@Data
public class VolunteerUpdateParams {


    /**
     * 志愿表id
     */
    private Long formId;

    /**
     * 修改后的志愿表名称
     */
    private String name;


}
