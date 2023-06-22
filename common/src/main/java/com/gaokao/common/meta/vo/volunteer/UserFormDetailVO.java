package com.gaokao.common.meta.vo.volunteer;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * @author attack204
 * date:  2021/8/8
 * email: 757394026@qq.com
 */
@Data
public class UserFormDetailVO {

    private Long id;

    //用户id
    private Long userId;

    //志愿表名称
    private String name;

    //用户分数
    private Integer score;

    //选课信息
    private List<Long> subject; // 用户的选课信息

    //志愿id
    private List<VolunteerVO> volunteerList;

    //生成类型
    private Boolean generatedType;

    //是否为当前表
    private Boolean current;

    //生成时间
    private Long generatedTime;

}
