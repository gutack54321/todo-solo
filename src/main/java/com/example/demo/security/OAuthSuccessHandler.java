package com.example.demo.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.example.demo.security.RedirectUrlCookieFilter.REDIRECT_URI_PARAM;

@Slf4j
@Component
@AllArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler { //OAuth 성공한 사용자에게 JWT발급

    private final String LOCAL_REDIRECT_URL = "http://localhost:3000";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("auth succeeded");
        TokenProvider tokenProvider = new TokenProvider();
        String token = tokenProvider.create(authentication);

        Optional<Cookie> oCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(REDIRECT_URI_PARAM)).findFirst();
        Optional<String> redirectUri = oCookie.map(Cookie::getValue);



        log.info("token {}", token);
//        response.getWriter().write(token); // 그냥 토큰은 전달. 우리서비스는 인증 성공시 todo페이지로 연결하고 따로 토큰을 줘야 함
//        response.sendRedirect("http://localhost:3000"); // 이러면 토큰을 줄 수 없다
//        response.sendRedirect("http://localhost:3000/sociallogin?token="+ token); // 프론트에서 sociallogin 페이지에서 url에 붙어있는 token을 로컬 스토리지에 저장 성공 시 메인화면, 토큰이 없을시 다시 로그인창
        response.sendRedirect(redirectUri.orElseGet(() -> LOCAL_REDIRECT_URL)+ "/sociallogin?token"+token);

    }
}
