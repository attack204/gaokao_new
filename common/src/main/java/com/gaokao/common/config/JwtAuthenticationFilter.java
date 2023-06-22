package com.gaokao.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaokao.common.enums.BusinessCode;
import com.gaokao.common.exceptions.BusinessException;
import com.gaokao.common.meta.AjaxResult;
import com.gaokao.common.meta.vo.user.LoginParams;
import com.gaokao.common.utils.JSONUtils;
import com.gaokao.common.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/xhr/v1/users/login", "POST"));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 从请求的 POST 中拿取 username 和 password 两个字段进行登入
        try {
            String body = StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset());
            LoginParams loginParams = JSONUtils.getMapper().readValue(body, LoginParams.class);
            log.info("[attemptAuthentication] username={}", loginParams.getUsername());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginParams.getUsername(), loginParams.getPassword());
            token.setDetails(this.authenticationDetailsSource.buildDetails(request));
            return getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new BusinessException("登陆参数非法");
        }

    }

    /*
    鉴权成功进行的操作，我们这里设置返回加密后的 token
    */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        handleResponse(request, response, authResult, null);
    }

    /*
    鉴权失败进行的操作，我们这里就返回 用户名或密码错误 的信息
    */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        handleResponse(request, response, null, failed);
    }

    private void handleResponse(HttpServletRequest request, HttpServletResponse response, Authentication authResult, AuthenticationException failed) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        AjaxResult<String> ajaxResult = new AjaxResult<>();
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        if (authResult != null) {
            // 处理登入成功请求
            User user = (User) authResult.getPrincipal();
            String token = JWTUtils.sign(user.getUsername(), user.getPassword());
            ajaxResult.setCode(BusinessCode.SUCCESS.getCode());
            ajaxResult.setMsg("登入成功");
            ajaxResult.setData("Bearer " + token);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(mapper.writeValueAsString(ajaxResult));
        } else {
            // 处理登入失败请求
            ajaxResult.setCode(BusinessCode.FAIL.getCode());
            ajaxResult.setMsg("用户名或密码错误");
            ajaxResult.setData(null);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(mapper.writeValueAsString(ajaxResult));
        }
    }
}
