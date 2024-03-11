package com.example.movie_review.domain.DTO;

import com.example.movie_review.domain.review.Review;
import com.example.movie_review.domain.review.ReviewImage;
import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewDto {
    private Long id;
    private String userNickname;
    private String userLoginId;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private LocalDateTime uploadDate;

    private String title; // 제목
    private String context; // 내용
    private Double score; // 평점
    private Integer heartCnt;
    private Long viewCount;

    private ReviewImage reviewImage;
    private String filePath;

    public static ReviewDto of(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .score(review.getScore())
                .userLoginId(review.getUser().getLoginId())
                .userNickname(review.getUser().getNickname())
                .title(review.getTitle())
                .context(review.getContext())
                .uploadDate(review.getUploadDate())
//                .createdAt(review.getCreatedAt())
//                .lastModifiedAt(review.getLastModifiedAt())
                .heartCnt(review.getHeartCnt())
                .reviewImage(review.getReviewImage())
                .viewCount(review.getViewCount())
                .build();
    }

}
