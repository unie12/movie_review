package com.example.movie_review.domain;

import com.example.movie_review.domain.ENUM.UserRole;
import com.example.movie_review.domain.review.Comment;
import com.example.movie_review.domain.review.Heart;
import com.example.movie_review.domain.review.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name_id")
    private String name;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "password")
    private String password;
    private String passwordCheck;

    private String nickname;

    @Embedded
    private Address address;

    private UserRole role;

    private Integer receivedHeartCnt;

    /**
     * 작성한 리뷰
     */
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Review> reviews;

    /**
     * user가 누른 좋아요
     */
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Heart> hearts;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Comment> comments;
    public void heartChange(Integer receivedHeartCnt) {
        this.receivedHeartCnt = receivedHeartCnt;
    }

    private String phoneNumber;
    private String gender;
    private String birth;
}
