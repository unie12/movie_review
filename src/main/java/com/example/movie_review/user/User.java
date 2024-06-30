package com.example.movie_review.user;

import com.example.movie_review.domain.Address;
import com.example.movie_review.domain.review.Comment;
import com.example.movie_review.domain.review.Heart;
import com.example.movie_review.domain.review.Review;
import jakarta.persistence.*;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // primary key

    @Column(name = "name_id", length = 30)
    private String name; // 이름

    @Column(name = "login_id", length = 30, unique = true)
    private String loginId; // 아이디

    @Column(name = "password")
    private String password; // 비밀번호
    private String passwordCheck; // 비밀번호 더블체크

    @Column(nullable = false, length = 30)
    private String nickname; // 닉네임

    @Embedded
    private Address address; // 주소
    private UserRole role; // 등급
    private Integer receivedHeartCnt; // 좋아요 받은 수
//    private Integer pressHeartCnt; // 좋아요 누른 수


    /**
     * 사용자 이용 현황
     */
    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /**
     * 추가 정보
     */
    private String phoneNumber;
    private String gender;
    private String birth;

    /**
     * 연관관계 메서드
     */
    public void addReview(Review review) {
        reviews.add(review);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

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

}
