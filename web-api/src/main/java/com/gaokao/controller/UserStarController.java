package com.gaokao.controller;

import com.gaokao.common.meta.AjaxResult;
import com.gaokao.common.meta.vo.advise.AdviseVO;
import com.gaokao.common.meta.vo.volunteer.VolunteerVO;
import com.gaokao.common.service.UserStarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


/**
 * @author MaeYon-Z
 * @create 2021-09-14
 */
@Slf4j
@RestController
@RequestMapping("/xhr/v1/stars")
public class UserStarController {

    @Autowired
    private UserStarService userStarService;

    @GetMapping("/list")
    public AjaxResult<Page<AdviseVO>> getUserStars(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                   @RequestParam(required = false, defaultValue = "5") Integer size) {
        return AjaxResult.SUCCESS(userStarService.getUserStarList(page, size));
    }

    @GetMapping("/star/{volunteerId}")
    public AjaxResult<Boolean> star(@PathVariable Long volunteerId) {
        return AjaxResult.SUCCESS(userStarService.star(volunteerId));
    }

}
