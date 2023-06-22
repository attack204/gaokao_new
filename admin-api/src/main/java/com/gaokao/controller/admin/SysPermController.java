package com.gaokao.controller.admin;

import com.gaokao.common.meta.AjaxResult;
import com.gaokao.common.meta.vo.admin.PermTreeVO;
import com.gaokao.common.meta.vo.admin.SysPermSaveParams;
import com.gaokao.common.service.admin.SysPermService;
import com.gaokao.common.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/xhr/v1/perms")
public class SysPermController {

    @Autowired
    private SysPermService sysPermService;

    /**
     * 创建权限接口
     *
     * @param params
     * @return
     */
    @PostMapping("/")
    @PreAuthorize("hasPermission('perm','add')")
    public AjaxResult<Long> create(@Valid @RequestBody SysPermSaveParams params) {
        return AjaxResult.SUCCESS(sysPermService.create(params));
    }

    /**
     * 更新权限信息
     *
     * @param params
     * @param id
     * @return
     */
    @PostMapping("update/{id}")
    @PreAuthorize("hasPermission('perm','update')")
    public AjaxResult<Long> update(@Valid @RequestBody SysPermSaveParams params,
                                   @PathVariable Long id) {
        Long result = sysPermService.update(id, params);
        if (result == -1) {
            return AjaxResult.FAIL("失败");
        } else {
            return AjaxResult.SUCCESS(result);
        }
    }

    @GetMapping("/")
    public AjaxResult<List<PermTreeVO>> list() {
        Long userId = UserUtils.getUserId();
        return AjaxResult.SUCCESS(sysPermService.list(userId));
    }

    /**
     * 删除权限
     *
     * @param id 传入要删除的权限的编号
     * @return 返回一个success字符串
     */
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasPermission('perm','delete')")
    public AjaxResult<String> delete(@PathVariable Long id) {
        if (sysPermService.deleteById(id) != -1)
            return AjaxResult.SUCCESSMSG("success");
        else
            return AjaxResult.FAIL("删除失败，权限不存在或出现其他异常");
    }

}
