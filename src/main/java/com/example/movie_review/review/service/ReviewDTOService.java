package com.example.movie_review.review.service;

import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.DTO.ReviewCommonDTO;
import com.example.movie_review.review.DTO.ReviewDTO;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.User;
import com.example.movie_review.user.service.UserDTOService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewDTOService {
    private final UserDTOService userDTOService;
    private final DbRatingService dbRatingService;

    public ReviewDTO createReviewDTO(Review review, String currentUserEmail, boolean isLikedByCurrentUser) {
        User user = review.getUser();
        UserCommonDTO userCommonDTO = userDTOService.getUserCommonDTO(user.getEmail());
        ReviewCommonDTO reviewCommonDTO = ReviewCommonDTO.builder()
                .id(review.getId())
                .text(review.getContext())
                .build();
        Double userRating = dbRatingService.getUserRatingForMovie(user.getId(), review.getDbMovies().getId());
        return new ReviewDTO(userRating, review.getHeartCount(), userCommonDTO, reviewCommonDTO, isLikedByCurrentUser);
    }
}