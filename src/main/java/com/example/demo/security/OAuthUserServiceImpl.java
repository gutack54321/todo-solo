package com.example.demo.security;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {  // 알아서 토큰 반환에 사용자 정보까지 준다.
    // 깃허브에서 받은 사용자 정보가 우리 앱에 존재여부 확인 후 없다면 계정생성

    @Autowired
    private UserRepository userRepository;

    public OAuthUserServiceImpl() {
        super();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        final OAuth2User oAuth2User = super.loadUser(userRequest); //DefaultOAuth2UserService에 구현된 loadUser를 통해 사용자 정보 받기

        try {
            log.info("OAuth2User attributes {} ", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes())); // OAuth2User 정보 확인용. 테스팅에만 사용
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }

        final String username = (String) oAuth2User.getAttributes().get("login");  //login 필드를 가져온다.
        final String authProvider = userRequest.getClientRegistration().getClientName();

        UserEntity userEntity= null;

        if(!userRepository.existsByUsername(username)){  //login에서 가져온 사용자가 우리 앱에 없다면
            userEntity = UserEntity.builder()
                    .username(username)
                    .authProvider(authProvider)
                    .build();
            userEntity = userRepository.save(userEntity);
        }
        else{
            userEntity = userRepository.findByUsername(username);
        }

        log.info("Successfully pulled user info username {} authProvider {}", username, authProvider);

        return new ApplicationOAuth2User(userEntity.getId(), oAuth2User.getAttributes());

    }



}
