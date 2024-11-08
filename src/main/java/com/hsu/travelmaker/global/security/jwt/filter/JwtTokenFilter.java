package com.hsu.travelmaker.global.security.jwt.filter;

import com.hsu.travelmaker.global.security.jwt.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 로그인 및 회원가입 경로를 필터링하지 않음
        String path = request.getRequestURI();
        if (path.equals("/api/signUp") || path.equals("/api/signIn")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String token = jwtTokenProvider.resolveToken(request);
        System.out.println("token: " + token);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            jwtTokenProvider.setSecurityContext(token);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}