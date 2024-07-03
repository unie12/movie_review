package com.example.movie_review.user;

import com.example.movie_review.domain.Address;
import com.example.movie_review.domain.review.Comment;
import com.example.movie_review.domain.review.Heart;
import com.example.movie_review.domain.review.Review;
import com.example.movie_review.genre.PreferredGenres;
import com.example.movie_review.movie.PreferredMovies;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Getter
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class User {

    /**
     * 구글 로그인 시 기본 데이터 저장
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // primary key

    private String name; // 이름
    private String email;
    private String picture;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role; // 등급

    /**
     * 추가 정보 입력
     */
    private String nickname; // 닉네임
    private String gender;
    private Long age;
    private String mbti;

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PreferredMovies> preferredMovies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<PreferredGenres> preferredGenres = new ArrayList<>();

    private Integer receivedHeartCnt; // 좋아요 받은 수
//    private Integer pressHeartCnt; // 좋아요 누른 수


    @Column(name = "login_id", length = 30, unique = true)
    private String loginId; // 아이디
    @Column(name = "password")
    private String password; // 비밀번호
    private String passwordCheck; // 비밀번호 더블체크

    /**
     * 사용자 이용 현황
     */
    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public User(String name, String email, String picture, String nickname, String gender, Long age, String mbti, UserRole role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.mbti = mbti;
//        this.preferGenres = preferGenres;
//        this.preferMovies = preferMovies;
        this.role = role;
    }


    /**
     * 연관관계 메서드
     */
    public void addReview(Review review) {
        reviews.add(review);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addPreferredMovie(PreferredMovies preferredMovie) { preferredMovies.add(preferredMovie); }

    public void addPreferredGenre(PreferredGenres preferredGenre) { preferredGenres.add(preferredGenre); }

    /**
     * 정보 수정
     */
    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateHeartCnt(Integer receivedHeartCnt) {
        this.receivedHeartCnt = receivedHeartCnt;
    }

//    public void updatePressHeartCnt(Integer pressHeartCnt) {
//        this.pressHeartCnt = pressHeartCnt;
//    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public User update(String name, String picture, String nickname, String gender, Long age, String mbti) {
        this.name = name;
        this.picture = picture;
        this.nickname = nickname != null ? nickname : this.nickname;
        this.gender = gender != null ? gender : this.gender;
        this.age = age != null ? age : this.age;
        this.mbti = mbti != null ? mbti : this.mbti;
//        this.preferGenres = preferGenres;
//        this.preferMovies = preferMovies;
        return this;
    }
}
