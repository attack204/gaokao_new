package com.gaokao.common.meta.vo.admin;

import lombok.Data;

/**
 * @author attack204
 * date:  2021/7/20
 * email: 757394026@qq.com
 */
@Data
public class ChangePwdVO {
    private String originPwd;
    private String newPwd;
}
