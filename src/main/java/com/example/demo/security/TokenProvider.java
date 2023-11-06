package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.model.UserEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "asdfadsfADFASDFRHGFJAsdfgioreadfkvbasdfjihuubjxcvhhauy23496kjdkjdvmz9dsflADFASDfkdifuasdf";

    public String create(UserEntity userEntity){
        // 기한 지금부터 1일
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS));

        // JWT Token 생성
//        return Jwts.builder()
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) //header alg: HS512사용
//                .setSubject(userEntity.getId()) //페이로드에 유저 id 대입
//                .setIssuer("demo app") // iss 토큰을 발생한 주체
//                .setIssuedAt(new Date()) // 발행날짜
//                .setExpiration(expiryDate) // 토큰 폐지 날짜
//                .compact();

        return "Bearer " + JWT.create()
                .withSubject(userEntity.getId())
                .withExpiresAt(expiryDate)
                .withIssuedAt(new Date())
                .withIssuer("demo app")
                .sign(Algorithm.HMAC512(SECRET_KEY));


    }

    public String create(final Authentication authentication){ // OAutuSuccessHandler에 사용하기위해
        ApplicationOAuth2User userPrincipal = (ApplicationOAuth2User) authentication.getPrincipal(); //새로운 oauth 사용자 정보로 받는다
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS));

        return "Bearer " + JWT.create()
                .withSubject(userPrincipal.getName()) // 이제는 ID를 가져올수 있다.
                .withExpiresAt(expiryDate)
                .withIssuedAt(new Date())
                .withIssuer("demo app")
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String validateAndGetUserId(String token){ // 토큰을 검증하고 올바른 토큰 시 페이로드를 가져온다
//        Claims claims =  Jwts.parser()
//                .setSigningKey(SECRET_KEY)  //2. 가져온 헤더, 페이로드,시크릿키를 이용해 서명 후 토큰에 있는 서명과 비교
//                .parseClaimsJwt(token)  // 1. 토큰 안에 있는 헤더, 페이로드 base 64로 디코딩하여 값을 가져옴
//                .getBody(); // 올바른 토큰 시 토큰의 페이로드를 가져온다.

        return JWT.decode(token).getSubject();

//        return claims.getSubject(); //토큰 서브젝트에 유저id 대입했었기 때문에 유저의 id를 알 수 있다.
    }

}
