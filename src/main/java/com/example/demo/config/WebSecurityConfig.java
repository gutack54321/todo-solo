package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.OAuthSuccessHandler;
import com.example.demo.security.OAuthUserServiceImpl;
import com.example.demo.security.RedirectUrlCookieFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private OAuthUserServiceImpl oAuthUserService;

    private OAuthSuccessHandler oAuthSuccessHandler;

    private RedirectUrlCookieFilter redirectUrlCookieFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()

                .httpBasic()
                    .disable()

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                    .antMatchers("/", "/auth/**").permitAll()

                .anyRequest()
                    .authenticated()
                .and()
                .oauth2Login()
                .redirectionEndpoint() // "localhost:8080/oauth2/callback/*으로 들어오는 요청은
                .baseUri("/oauth2/callback/*") //redirectionEndpoint에 설정한곳으로 보내기

                .and()
                .authorizationEndpoint() // 깃허브에서 기본 제공  8080/oauth2/authorization/github로 깃허브 로그인 실행
                .baseUri("/auth/authorize")   // 다른 oauth2사용을 위해 provider 구분하기 위해 기본을 /auth/authorize/{provider} 형태로 수정

                .and()
                .userInfoEndpoint()
                .userService(oAuthUserService) // 사용자가 존재유무에 맞춰 계정 생성해주는 서비스 연결
                .and()
                .successHandler(oAuthSuccessHandler) //인증 성공이후 핸들러, 인증성공한 사용자에게 JWT발행
                .and()
                .exceptionHandling() // 예외 발생시 어떻게 핸들링할 것인지(아무 설정 없으면 spring security가 302를 반환)
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint()) //실패시 403를 반환
        ;



        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);  //위에 http필터 돌고 난 후 작동할 필터

        http.addFilterBefore(redirectUrlCookieFilter, OAuth2AuthorizationRequestRedirectFilter.class); //위에 http필터 돌기 전 작동할 필터

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);

        return source;
    }

}
