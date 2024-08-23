package com.example.movie_review.oauth;

import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.repository.UserRepository;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;
//    private static final List<String> ALLOWED_DOMAINS = Arrays.asList("ajou.ac.kr");

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
//        if (!ALLOWED_DOMAINS.contains(domain)) {
//            throw new OAuth2AuthenticationException("Unauthorized domain" + domain);
//        }
        // 데이터베이스에서 추가 정보 조회 및 반영
        Optional<User> userOptional = userRepository.findByEmail(attributes.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            attributes.setNickname(user.getNickname());
            attributes.setGender(user.getGender());
            attributes.setAge(user.getAge());
            attributes.setMbti(user.getMbti());
            attributes.setPicture(user.getPicture());
            // 필요한 경우 preferGenres와 preferMovies도 반영
        }

        User user = saveOrUpdate(attributes);

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

        return userRepository.save(user);
    }

    private User updateUser(User user, OAuthAttributes attributes) {
//        long l = System.currentTimeMillis();
//        String nickname = "" + l;

        user.setName(attributes.getName());
        user.setPicture(attributes.getPicture());
        // 기존 사용자의 경우에만 추가 정보 업데이트
        if (user.getNickname() != null) {
            user.setNickname(attributes.getNickname() != null ? attributes.getNickname() : user.getNickname());
            user.setGender(attributes.getGender() != null ? attributes.getGender() : user.getGender());
            user.setAge(attributes.getAge() != null ? attributes.getAge() : user.getAge());
            user.setMbti(attributes.getMbti() != null ? attributes.getMbti() : user.getMbti());
        }
//        user.setNickname(attributes.getNickname() != null ? attributes.getNickname() : nickname);
//        user.setGender(attributes.getGender() != null ? attributes.getGender() : "미정");
//        user.setAge(attributes.getAge() != null ? attributes.getAge() : 0L);
//        user.setMbti(attributes.getMbti() != null ? attributes.getMbti() : "Pretty");

        return user;
    }
}