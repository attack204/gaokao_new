package com.gaokao.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaokao.common.config.JwtAuthenticationFilter;
import com.gaokao.common.config.JwtAuthorizationFilter;
import com.gaokao.common.meta.AjaxResult;
import com.gaokao.common.service.admin.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    private SysUserService sysUserService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(sysUserService);
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
                        "/",
                        "/index.html",
                        "/static/**",
                        "/kLmkiG7eeH.txt",
                        "/xhr/v1/users/login",
                        "/xhr/v1/users/logout",
                        "/xhr/v1/users/create",
                        "/xhr/v1/users/needLogin",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/xhr/v1/orders/wxPayNotify",
                        "/xhr/v1/orders/wxRefundNotify",
                        "/webjars/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/xhr/v1/users/needLogin")
                .loginProcessingUrl("/xhr/v1/users/login")
                .and()
                .logout()
                .logoutUrl("/xhr/v1/users/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    ObjectMapper mapper = new ObjectMapper();
                    AjaxResult<String> ajaxResult = AjaxResult.SUCCESSMSG("登出成功");
                    response.setHeader("Content-Type", "application/json;charset=UTF-8");
                    response.setStatus(HttpStatus.OK.value());
                    response.getWriter().write(mapper.writeValueAsString(ajaxResult));
                })
                .permitAll()
                .and()
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), sysUserService))
                // 前后端分离是 STATELESS，故 session 使用该策略
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
