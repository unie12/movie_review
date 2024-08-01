package com.example.movie_review.oauth;

import com.example.movie_review.user.User;
import com.example.movie_review.user.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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
//    private List<String> preferGenres;

//    private List<String> preferMovies = new ArrayList<>();
//    private String preferDirector;
//    private String preferActor;


    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture, String nickname,
                           String gender, Long age, String mbti){
//                           List<String> preferMovies) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.nickname = nickname != null ? nickname : "기본 닉네임"; // 기본값 설정
        this.gender = gender != null ? gender : "미정";
        this.age = age != null ? age : 0L;
        this.mbti = mbti != null ? mbti : "Pretty";
//        this.preferGenres = preferGenres != null ? preferGenres : new ArrayList<>();
//        this.preferMovies = preferMovies != null ? preferMovies : new ArrayList<>();
    }

    public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String usernameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .nickname((String) attributes.get("nickname")) // 닉네임 필드 추가
                .gender((String) attributes.get("gender")) // 성별 필드 추가
                .age(attributes.get("age") != null ? Long.valueOf(attributes.get("age").toString()) : null) // 나이 필드 추가
                .mbti((String) attributes.get("mbti")) // MBTI 필드 추가
                .attributes(attributes)
                .nameAttributeKey(usernameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(UserRole.BRONZE)
                .nickname(nickname) // 닉네임 필드 추가
                .gender(gender)
                .age(age)
                .mbti(mbti)
//                .preferGenres(preferGenres)
//                .preferMovies(preferMovies)
                .build();
    }
}
