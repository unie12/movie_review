package com.example.movie_review.review.service;

import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.movieDetail.DTO.MovieDetailDTO;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.DTO.ReviewCommonDTO;
import com.example.movie_review.review.DTO.ReviewDTO;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserDTOService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewDTOService {
    private final UserDTOService userDTOService;
    private final DbRatingService dbRatingService;

    public ReviewDTO createReviewDTO(Review review, String currentUserEmail, boolean isLikedByCurrentUser) {
        User user = review.getUser();
        UserCommonDTO userCommonDTO = userDTOService.getUserCommonDTO(user);
        ReviewCommonDTO reviewCommonDTO = ReviewCommonDTO.builder()
                .id(review.getId())
                .text(review.getContext())
                .build();
        Double userRating = dbRatingService.getUserRatingForMovie(user.getId(), review.getDbMovies().getId());
        return new ReviewDTO(userRating, review.getHeartCount(), userCommonDTO, reviewCommonDTO, isLikedByCurrentUser, review.isSpoiler());
    }

    public List<ReviewDTO> getSortedReviews(MovieDetailDTO movieDetailDTO) {
        List<ReviewDTO> sortedReviews = movieDetailDTO.getReviews().stream()
                .sorted((r1, r2) -> Integer.compare(r2.getHeartCnt(), r1.getHeartCnt()))
                .limit(6)
                .collect(Collectors.toList());

        sortedReviews.forEach(reviewDTO -> {
            String reviewTextWithBreaks = reviewDTO.getReview().getText().replace("\n", "<br>");
            reviewDTO.getReview().setText(reviewTextWithBreaks);
        });

        return sortedReviews;
    }
}