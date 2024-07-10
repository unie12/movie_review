package com.example.movie_review.user;

import com.example.movie_review.dbMovie.DbRatings;
import com.example.movie_review.Comment.Comment;
import com.example.movie_review.Heart.Heart;
import com.example.movie_review.review.Review;
import com.example.movie_review.favoriteMovie.UserFavoriteMovie;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PreferredMovies> preferredMovies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferredGenres> preferredGenres = new ArrayList<>();


    /**
     * 사용자 이용 현황
     */
    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<UserFavoriteMovie> userFavoriteMovies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<DbRatings> dbRatings;

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



    public String getRoleKey() {
        return this.role.getKey();
    }

    public User update(String nickname, String gender, Long age, String mbti) {
        this.nickname = nickname != null ? nickname : this.nickname;
        this.gender = gender != null ? gender : this.gender;
        this.age = age != null ? age : this.age;
        this.mbti = mbti != null ? mbti : this.mbti;
        return this;
    }
}
