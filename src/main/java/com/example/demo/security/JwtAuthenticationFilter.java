package com.example.demo.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { //OncePerRequestFilter는 한번 수행하면 다시 안한다.

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request); // http 요청의 헤더에 존재하는 토큰 가져오기
            log.info("Filter is running...");

            if(token !=null && !token.equalsIgnoreCase("null")){ // 토큰이 존재하고 null이 아니라면
                String userId = tokenProvider.validateAndGetUserId(token); // 토큰안에 있는 유저id 가져오기
                log.info("Authenticated user ID : " + userId);

                // SecurityContextHolder에 등록하기

                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( // 인증된 사용자 정보.(userDetails)
                        userId, // AuthenticationPrincipal
                        null, AuthorityUtils.NO_AUTHORITIES);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 잘모르겠다.
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // securityContext 생성 후
                securityContext.setAuthentication(authentication);  // userDetails를 가진 authenticationToken를 securityContext에 담고
                SecurityContextHolder.setContext(securityContext);  // securityContext를 securityContextHolder에 넣어준다.

            }
        }
        catch (Exception ex){
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);

    }

    private String parseBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization"); // http 요청의 헤더에 토큰을 가져오기

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){  // 토큰 안 값이 있고 bearer 로 시작하면
            return bearerToken.substring(7);  // 진짜 토큰 내용만 따로 떼어서 리턴
        }

        return null;
    }

}
