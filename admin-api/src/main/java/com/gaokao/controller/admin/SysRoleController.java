package com.gaokao.controller.admin;

import com.gaokao.common.meta.AjaxResult;
import com.gaokao.common.meta.vo.admin.SysRoleSaveParams;
import com.gaokao.common.meta.vo.admin.SysRoleVO;
import com.gaokao.common.meta.vo.common.NameValuePair;
import com.gaokao.common.service.admin.SysRoleService;
import com.gaokao.common.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author attack204
 * date:  2021/7/20
 * email: 757394026@qq.com
 */
@Slf4j
@RestController
@RequestMapping("/xhr/v1/roles")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/")
    @PreAuthorize("hasPermission('role','add')")
    public AjaxResult<Long> create(@Valid @RequestBody SysRoleSaveParams params) {
        return AjaxResult.SUCCESS(sysRoleService.insert(params, UserUtils.getCorpId(), UserUtils.getUserName()));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasPermission('role','update')")
    public AjaxResult<Long> update(@Valid @RequestBody SysRoleSaveParams params,
                                   @PathVariable Long id) {
        return AjaxResult.SUCCESS(sysRoleService.update(id, params));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('role','delete')")
    public AjaxResult<Long> delete(@PathVariable Long id) {
        Long result = sysRoleService.delete(id);
        if (result == -1) {
            return AjaxResult.FAIL("用户不存在或其他异常");
        } else {
            return AjaxResult.SUCCESS(result);
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasPermission('role','view')")
    public AjaxResult<Page<SysRoleVO>> list(@RequestParam(required = false, defaultValue = "") String keyword,
                                            @RequestParam(required = false, defaultValue = "1") Integer page,
                                            @RequestParam(required = false, defaultValue = "10") Integer size) {
        Long operatorCorpId= UserUtils.getCorpId();
        return AjaxResult.SUCCESS(sysRoleService.list(keyword, UserUtils.getCorpId(), operatorCorpId, page, size));
    }

    @GetMapping("/dropdown")
    @PreAuthorize("hasPermission('role','view')")
    public AjaxResult<List<NameValuePair>> dropdown() {
        Long corpId = UserUtils.getCorpId();
        return AjaxResult.SUCCESS(sysRoleService.list(corpId));
    }

}
