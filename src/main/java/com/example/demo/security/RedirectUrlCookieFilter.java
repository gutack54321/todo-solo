package com.example.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RedirectUrlCookieFilter extends OncePerRequestFilter {
    public static final String REDIRECT_URI_PARAM = "redirect_url";
    private static final int MAX_AGE = 180;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/auth/authorize")){
            try {
                log.info("request url {} ", request.getRequestURI());
                String redirectUrl = request.getParameter(REDIRECT_URI_PARAM); //프론트의 socialLogin에서 redirect_url=에 가진 정보를 가져오기

                Cookie cookie = new Cookie(REDIRECT_URI_PARAM, redirectUrl); //쿠키에 저장하기
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(MAX_AGE);
                response.addCookie(cookie); //response에 쿠키를 추가하여
            }
            catch (Exception ex){
                logger.error("Could not set user authentication in security context", ex);
                log.info("Unauthorized request");
            }
        }
        filterChain.doFilter(request, response);  //다음 필터로 넘어간다.
    }
}
