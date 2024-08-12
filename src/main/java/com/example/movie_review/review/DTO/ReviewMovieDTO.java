package com.example.movie_review.review.DTO;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
public class ReviewMovieDTO{
    private MovieCommonDTO movieCommonDTO;
    private ReviewDTO reviewDTO;

    private String original_title;
    private LocalDateTime reviewDate;
    private LocalDateTime heartDate;
    private int heartCnt;

    private String filter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewMovieDTO that = (ReviewMovieDTO) o;
        return Objects.equals(reviewDTO.getReview().getId(), that.reviewDTO.getReview().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewDTO.getReview().getId());
    }

    //    /**
//     * Review 관련
//     */
//    private Long id;
//    private String context;
//    private int heartCount;
//    /**
//     * 해당 review 작성 사용자 관련
//     */
//    private String email;
//    private String nickname;
//    private String userPicture;
//    private Double userRating;
//
//    /**
//     * 해당 review 작성된 영화 관련
//     */
//    private Long movieId;
//    private Integer tId;
//    private String title;
//    private String poster_path;
//    private String original_title;
//
//    /**
//     * 현재 로그인한 사용자가 이 리뷰에 좋아요를 눌렀는지 여부
//     */
//    private boolean isLikedByCurrentUser;
}
