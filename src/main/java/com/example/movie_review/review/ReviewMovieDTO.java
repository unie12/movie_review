package com.example.movie_review.review;

import com.example.movie_review.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewMovieDTO {
    /**
     * Review 관련
     */
    private Long id;
    private String context;
    private int heartCount;
    /**
     * 해당 review 작성 사용자 관련
     */
    private String email;
    private String nickname;
    private String userPicture;
    private Double userRating;

    /**
     * 해당 review 작성된 영화 관련
     */
    private Long movieId;
    private Integer tId;
    private String title;
    private String poster_path;
    private String original_title;
}
