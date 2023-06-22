package com.gaokao.common.meta.bo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
public class JwtUser extends User {

    private Long corp;

    private Long id;

    private Map<String, Set<String>> perms;


    public JwtUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCorp() {
        return corp;
    }

    public void setCorp(Long corp) {
        this.corp = corp;
    }

    public Map<String, Set<String>> getPerms() {
        return perms;
    }

    public void setPerms(Map<String, Set<String>> perms) {
        this.perms = perms;
    }
}
