package com.gaokao.common.meta.vo.volunteer;

import com.gaokao.common.meta.po.UserForm;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * @author attack204
 * date:  2021/8/23
 * email: 757394026@qq.com
 */
@Data
public class UserFormAllVO {

    private Long id;

    private Long userId;

    private String name;

    private Integer score;

    private List<Long> subject;

    private Boolean generatedType;

    private Boolean current;

    private Long generatedTime;

}
