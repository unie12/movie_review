package com.example.movie_review.oauth;

import com.example.movie_review.user.User;
import com.example.movie_review.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private static final List<String> ALLOWED_DOMAINS = Arrays.asList("ajou.ac.kr");

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(userNameAttributeName, oAuth2User.getAttributes());

        String email = attributes.getEmail();
        String domain = email.substring(email.indexOf("@") + 1);
        if (!ALLOWED_DOMAINS.contains(domain)) {
            throw new OAuth2AuthenticationException("Unauthorized domain" + domain);
        }
        // 데이터베이스에서 추가 정보 조회 및 반영
        System.out.println("sevice Attributes Map: " + attributes);
        Optional<User> userOptional = userRepository.findByEmail(attributes.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            attributes.setNickname(user.getNickname());
            attributes.setGender(user.getGender());
            attributes.setAge(user.getAge());
            attributes.setMbti(user.getMbti());
            // 필요한 경우 preferGenres와 preferMovies도 반영
        }

        User user = saveOrUpdate(attributes);
        System.out.println("loaddd user = " + user.getNickname());

        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> updateUser(entity, attributes))
                .orElse(attributes.toEntity());
        System.out.println("Saving user: " + user);

        return userRepository.save(user);
    }

    private User updateUser(User user, OAuthAttributes attributes) {
        System.out.println("att val = " + attributes.getNickname());
        System.out.println("user val = " + user.getNickname());
        user.setName(attributes.getName());
        user.setPicture(attributes.getPicture());
        user.setNickname(attributes.getNickname() != null ? attributes.getNickname() : "기본 닉네임");
        user.setGender(attributes.getGender() != null ? attributes.getGender() : "미정");
        user.setAge(attributes.getAge() != null ? attributes.getAge() : 0L);
        user.setMbti(attributes.getMbti() != null ? attributes.getMbti() : "Pretty");
        // 필요한 경우 preferGenres와 preferMovies도 업데이트
        System.out.println("Updated user: " + user);

        return user;
    }
}