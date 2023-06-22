package com.gaokao.controller.user;

import com.gaokao.common.dao.UserMemberDao;
import com.gaokao.common.enums.VeryCodeType;
import com.gaokao.common.meta.AjaxResult;
import com.gaokao.common.meta.bo.JwtUser;
import com.gaokao.common.meta.po.UserMember;
import com.gaokao.common.meta.vo.user.MemberUpdateParams;
import com.gaokao.common.meta.vo.user.RegParams;
import com.gaokao.common.meta.vo.user.UserMemberVO;
import com.gaokao.common.meta.vo.user.UserUpdateParams;
import com.gaokao.common.service.UserMemberService;
import com.gaokao.common.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author attack204
 * date:  2021/7/20
 * email: 757394026@qq.com
 */
@ResponseBody
@Slf4j
@RestController
@RequestMapping("/xhr/v1/userMember")
public class UserMemberController {
    @Autowired
    private UserMemberService userMemberService;



    @GetMapping("/needLogin")
    public AjaxResult<String> needLogin() {
        return AjaxResult.UNAUTHORIZED("请登陆或者注册");
    }

    @GetMapping("/sendVerifyCode")
    public AjaxResult<String> sendVerifyCode(String phone) {
        return AjaxResult.SUCCESSMSG(userMemberService.sendVerifyCode(VeryCodeType.REG, phone));
    }

    /**
     * 更新用户信息 通过Authentication.getPrincipal()可以获取到代表当前用户的信息
     */
    @PostMapping("/update/{id}")
    public AjaxResult<Long> update(@PathVariable Long id,
                                   @Valid @RequestBody MemberUpdateParams params) {
            return AjaxResult.SUCCESS(userMemberService.update(id, params));

    }

    @PostMapping("/reg")
    public AjaxResult<Long> reg(@Valid @RequestBody RegParams regParams) {
        return AjaxResult.SUCCESS(userMemberService.reg(regParams));
    }

    @GetMapping("/info")
    public AjaxResult<UserMemberVO> Current() {
        return AjaxResult.SUCCESS(userMemberService.getInfo(UserUtils.getUserId()));
    }

    @PostMapping("/lock/{id}")
    @PreAuthorize("hasPermCurrentsion('usermember','update')")
    public AjaxResult<Long> lock(@PathVariable Long id) {
        Long result = userMemberService.lock(id);
        if(result == -1) {
            return AjaxResult.FAIL("用户不存在或其他异常");
        } else {
            return AjaxResult.SUCCESS(result);
        }
    }

    @PostMapping("/unlock/{id}")
    @PreAuthorize("hasPermission('usermember','update')")
    public AjaxResult<Long> unlock(@PathVariable Long id) {
        Long result = userMemberService.unlock(id);
        if(result == -1) {
            return AjaxResult.FAIL("用户不存在或其他异常");
        } else {
            return AjaxResult.SUCCESS(result);
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasPermission('usermember','view')")
    public AjaxResult<Page<UserMemberVO>> list(@RequestParam(required = false, defaultValue = "") String keyword,
                                           @RequestParam(required = false, defaultValue = "1") Integer page,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        return AjaxResult.SUCCESS(userMemberService.list(keyword, page, size));
    }




}
