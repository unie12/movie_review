package com.example.movie_review.domain.review;

import com.example.movie_review.domain.BaseEntity;
import com.example.movie_review.domain.DTO.ReviewCreateRequest;
import com.example.movie_review.domain.DTO.ReviewDto;
import com.example.movie_review.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private LocalDateTime uploadDate = LocalDateTime.now(); // 글 작성일
    private String title; // 제목
    private String context; // 내용

    private String writer; // 닉네임
    private Double score; // 평점
    private Long viewCount; // 조회수

    /**
     * 매핑 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<Heart> hearts;
    private Integer heartCnt;

//    /**
//     * 리뷰에 올릴 이미지
//     */
//    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    private List<ReviewImage> reviewImages;

    @OneToOne(fetch = FetchType.LAZY)
    private ReviewImage reviewImage;
    private String filePath;

    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    private Integer commentCnt;


    /**
     * 리뷰 값 생성
     */
    public void updateReview(ReviewCreateRequest reviewCreateRequest, User user) {
        this.title = reviewCreateRequest.getTitle();
        this.writer = user.getNickname();
        this.user = user;
        this.context = reviewCreateRequest.getContext();
        this.score = reviewCreateRequest.getScore();
        this.heartCnt = 0;
        this.viewCount = 0L;
        this.commentCnt = 0;
    }
    public void update(ReviewDto dto) {
        this.title = dto.getTitle();
        this.context = dto.getContext();
        this.score = dto.getScore();
    }

    /**
     * 리뷰 수정
     */
    public void editReview(ReviewDto req) {
        this.title = req.getTitle();
        this.context = req.getContext();
        this.score = req.getScore();
    }
    /**
     * 연관관계 메서드
     */
    public void confirmUser(User user) {
        this.user = user;
        user.addReview(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    /**
     * 내용 수정
     */
    public void updateCommentCnt(Integer commentCnt) {
        this.commentCnt = commentCnt;
    }

    public void updateHeartCnt(Integer heartCnt) {
        this.heartCnt = heartCnt;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContext(String context) {
        this.context = context;
    }

    public void updateFilePath(String filePath) {
        this.filePath = filePath;
    }

}
