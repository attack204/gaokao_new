package com.gaokao.common.config;

import com.gaokao.common.meta.bo.JwtUser;
import com.gaokao.common.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Slf4j
public class AppPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            JwtUser user = (JwtUser) principal;
            if (user.getPerms() == null) {
                return false;
            }
            Set<String> permCodes = user.getPerms().get((String) targetDomainObject);
            if (permCodes == null) {
                return false;
            }
            return permCodes.contains((String) permission);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new BusinessException("unsupport perm check type");
    }
}
