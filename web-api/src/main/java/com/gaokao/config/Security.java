package com.gaokao.config;

import com.gaokao.common.config.JwtAuthenticationFilter;
import com.gaokao.common.config.JwtAuthorizationFilter;
import com.gaokao.common.service.UserMemberService;
import com.gaokao.common.service.UserMemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

/**
 * @author attack204
 * date:  2021/8/4
 * email: 757394026@qq.com
 */
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMemberService userMemberService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userMemberService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }


    /**
     * 认证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth.eraseCredentials(false);
    }

    /**
     * 授权
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁止隧道 // 禁止跨域 // 禁止头部
        http.csrf().disable().cors().disable().headers().disable();
        http.addFilterAt(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers(
                    //    "/static/**",
                        "/xhr/v1/addresses/**",
                        "/xhr/v1/orders/**",
                        "/xhr/v1/users/findWxUser",
                        "/xhr/v1/wechat/update",
                        "/xhr/v1/favorites/getFavoriteShops",
                        "/xhr/v1/favorites/getFavoriteGoods",
                        "/xhr/v1/favorites/create",
                        "/xhr/v1/favorites/delete",
                        "/xhr/v1/favorites/deleteByType").hasAuthority("HasLoggedIn")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/xhr/v1/users/login")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write("{\"code\":401,\"msg\":\"请先登录!\"}");
                    out.flush();
                    out.close();
                })
                .and()
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userMemberService))
                // 前后端分离是 STATELESS，故 session 使用该策略
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
