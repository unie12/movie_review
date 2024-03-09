package com.example.movie_review.domain.review;

import com.example.movie_review.domain.DTO.ReviewCreateRequest;
import com.example.movie_review.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private LocalDateTime uploadDate = LocalDateTime.now();
    private String title;
    private String writer;
    private String context;
    private Long good = 0L;
    private Double score;
    private Long viewCount;

    /**
     * 사용자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * 좋아요
     */
    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<Heart> hearts;
    private Integer heartCnt;

//    /**
//     * 리뷰에 올릴 이미지
//     */
//    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    @OrderBy("id asc")
//    private List<ReviewImage> reviewImages;
    @OneToOne(fetch = FetchType.LAZY)
    private ReviewImage reviewImage;

    /**
     * 댓글
     */
    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<Comment> comments;
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

    public void commentChange(Integer commentCnt) {
        this.commentCnt = commentCnt;
    }

    public void heartChange(Integer heartCnt) {
        this.heartCnt = heartCnt;
    }
}
