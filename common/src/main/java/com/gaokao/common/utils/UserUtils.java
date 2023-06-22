package com.gaokao.common.utils;

import com.gaokao.common.meta.bo.JwtUser;
import com.gaokao.common.constants.AppConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Objects;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
public class UserUtils {
    /**
     * 获取当前登陆用户名
     *
     * @return username
     */
    public static String getUserName() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                user = (User) principal;
            }
        }
        if (user == null) {
            return null;
        }
        return user.getUsername();
    }


    public static Long getCorpId() {
        JwtUser user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                user = (JwtUser) principal;
            }
        }
        if (user == null) {
            return null;
        }
        return user.getCorp();
    }

    public static Long getUserId() {
        JwtUser user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                user = (JwtUser) principal;
            }
        }
        if (user == null) {
            return null;
        }
        return user.getId();
    }


    public static boolean isPlatformUser(Long corpId) {
        return corpId != null && corpId.equals(AppConstant.ADMIN_CORP) || corpId != null && corpId.equals(AppConstant.GENERAL_ADMIN_CORP);
    }
    public static boolean isPlatformUser() {
        Long corpId = getCorpId();
        return corpId != null && corpId.equals(AppConstant.ADMIN_CORP) || corpId != null && corpId.equals(AppConstant.GENERAL_ADMIN_CORP);
    }
}
