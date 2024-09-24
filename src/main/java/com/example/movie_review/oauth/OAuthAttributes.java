package com.example.movie_review.oauth;

import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    private String nickname;
    private String gender;
    private Long age;
    private String mbti;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture, String nickname,
                           String gender, Long age, String mbti) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.mbti = mbti;
    }

    public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String usernameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture("/images/defaultPerson.png") // 여기를 수정
                .attributes(attributes)
                .nameAttributeKey(usernameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .nickname("temp_" + email.split("@")[0])
                .email(email)
                .picture(picture)
                .role(UserRole.BRONZE)
                .build();
    }
}
