package com.gaokao.controller.admin;

import com.gaokao.common.constants.AppConstant;
import com.gaokao.common.meta.AjaxResult;
import com.gaokao.common.meta.bo.JwtUser;
import com.gaokao.common.meta.bo.UserInfo;
import com.gaokao.common.meta.vo.admin.*;
import com.gaokao.common.service.admin.SysUserService;
import com.gaokao.common.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author attack204
 * date:  2021/7/20
 * email: 757394026@qq.com
 */
@Slf4j
@RestController
@RequestMapping("/xhr/v1/users")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/needLogin")
    public AjaxResult<String> needLogin() {
        return AjaxResult.UNAUTHORIZED("请登陆或者注册");
    }

    @GetMapping("/logout")
    public AjaxResult<Boolean> logout() {
        return AjaxResult.SUCCESS(true);
    }


    @PreAuthorize("hasPermission('user','add')")
    @PostMapping("/create")
    public AjaxResult<Long> create(@Valid @RequestBody SysUserCreateParams params) {
        Long corpId = UserUtils.getCorpId();
        if (!AppConstant.GENERAL_ADMIN_CORP.equals(corpId)) {
            params.setCorp(corpId);
        }
        return AjaxResult.SUCCESS(sysUserService.create(params));
    }

    /**
     * 更新用户信息
     * !!注意不能更新密码！，修改密码请访问changPwd接口
     *
     * @param params 用户参数
     * @param id     用户id
     * @return 成功则返回编号
     */
    @PreAuthorize("hasPermission('user','update')")
    @PostMapping("/update/{id}")
    public AjaxResult<Long> update(@Valid @RequestBody SysUserUpdateParams params,
                                   @PathVariable Long id) {
        Long result = sysUserService.updateById(params, id);
        if (result == -1) {
            return AjaxResult.FAIL("更新失败，用户不存在或出现其他异常");
        } else {
            return AjaxResult.SUCCESS(result);
        }
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasPermission('user','delete')")
    public AjaxResult<Long> delete(@PathVariable Long id) {
        Long result = sysUserService.deleteById(id);
        if (result == -1) {
            return AjaxResult.FAIL("删除失败，用户不存在或用户名重复或出现其他异常");
        } else {
            return AjaxResult.SUCCESS(result);
        }

    }

    @GetMapping("/")
    @PreAuthorize("hasPermission('user','view')")
    public AjaxResult<Page<UserVO>> list(@RequestParam(required = false, defaultValue = "") String keyword,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        Long corpId = UserUtils.getCorpId();
        return AjaxResult.SUCCESS(sysUserService.list(keyword, page, size, corpId));
    }

    /**
     * 传入的Roles会覆盖之前的Roles
     *
     * @param allocUserRoles
     * @return
     */
    @PostMapping("/allocRoles")
    @PreAuthorize("hasPermission('user','edit')")
    public AjaxResult<Boolean> allocRoles(@Valid @RequestBody AllocUserRoles allocUserRoles) {
        return AjaxResult.SUCCESS(sysUserService.allocRoles(allocUserRoles.getUserId(), allocUserRoles.getRoleIds()));
    }

    @GetMapping("/info")
    public AjaxResult<UserInfo> info(Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return AjaxResult.SUCCESS(sysUserService.getInfo(jwtUser.getId()));
    }

    @PostMapping("/changePwd")
    public AjaxResult<UserInfo> changePwd(@RequestBody ChangePwdVO changePwdVO, Authentication authentication) {
        log.info("[changePwd] begin changePwd");
        if (StringUtils.isEmpty(changePwdVO.getOriginPwd()) || StringUtils.isEmpty(changePwdVO.getNewPwd())) {
            return AjaxResult.FAIL("参数非法");
        }
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        int code = sysUserService.changePwd(jwtUser.getId(), changePwdVO.getOriginPwd(), changePwdVO.getNewPwd());
        if (code == -1) {
            log.info("[changePwd] failed.");
            return AjaxResult.FAIL("用户不存在");
        } else if (code == 0) {
            return AjaxResult.FAIL("旧密码错误");
        } else {
            log.info("[changePwd] success.");
            return AjaxResult.SUCCESS(sysUserService.getInfo(jwtUser.getId()));
        }
    }

}
