package com.example.movie_review.review;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ReviewMovieDTO{

    private MovieCommonDTO movieCommonDTO;
    private ReviewDTO reviewDTO;
    private boolean isLikedByCurrentUser;
    private String original_title;
    private LocalDateTime reviewDate;



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
