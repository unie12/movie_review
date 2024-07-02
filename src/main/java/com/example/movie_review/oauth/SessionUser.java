package com.example.movie_review.oauth;

import com.example.movie_review.user.User;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;
    private String nickname;
    private String gender;
    private Long age;
    private String mbti;
//    private List<String> preferGenres;
//    private List<String> preferMovies;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.nickname = user.getNickname();
        this.gender = user.getGender();
        this.age = user.getAge();
        this.mbti = user.getMbti();
//        this.preferGenres = user.getPreferGenres();
//        this.preferMovies = user.getPreferMovies();
    }
}
