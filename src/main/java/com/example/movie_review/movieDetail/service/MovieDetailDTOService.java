package com.example.movie_review.movieDetail.service;

import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.favoriteMovie.UserFavoriteMovieService;
import com.example.movie_review.heart.HeartService;
import com.example.movie_review.movieDetail.DTO.MovieBasicInfo;
import com.example.movie_review.movieDetail.DTO.MovieDetailDTO;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.DTO.ReviewCommonDTO;
import com.example.movie_review.review.DTO.ReviewDTO;
import com.example.movie_review.review.service.ReviewService;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserDTOService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieDetailDTOService {
    private final DbMovieService dbMovieService;
    private final UserFavoriteMovieService userFavoriteMovieService;
    private final ReviewService reviewService;
    private final HeartService heartService;
    private final DbRatingService dbRatingService;
    private final TmdbService tmdbService;
    private final UserDTOService userDTOService;

    private final MovieBasicService movieBasicService;

    public MovieDetailDTO getMovieDetailDTO(Long movieTId, Authentication principal) {
        MovieBasicInfo basicInfo = movieBasicService.getMovieBasicInfo(movieTId);
        DbMovies dbMovie = dbMovieService.findOrCreateMovie(movieTId);

        List<Review> reviews = reviewService.findReviewByDbMovies(dbMovie);
        List<ReviewDTO> reviewDTOS = reviews.stream()
                .map(review -> {
                    User user = review.getUser();
                    UserCommonDTO userCommonDTO = userDTOService.getUserCommonDTO(user.getEmail());
                    ReviewCommonDTO reviewCommonDTO = ReviewCommonDTO.builder()
                            .id(review.getId())
                            .text(review.getContext())
                            .build();
                    boolean isLikedByCurrentUser = reviewService.isLikedByCurrentUser(review, principal.getName());
                    Double userRating = dbRatingService.getUserRatingForMovie(user.getId(), dbMovie.getId());
                    return new ReviewDTO(userRating, review.getHeartCount(),userCommonDTO, reviewCommonDTO, isLikedByCurrentUser);
                })
                .collect(Collectors.toList());

        return MovieDetailDTO.builder()
                .basicInfo(basicInfo)
                .reviewCnt(dbMovie.getReviewCount())
                .ajou_ratingAvg(dbMovie.getDbRatingAvg())
                .ajou_ratingCnt(dbMovie.getDbRatingCount())
                .reviews(reviewDTOS)
                .userHearts(heartService.getUserHearts(principal.getName()))
                .isFavorite(principal != null ? userFavoriteMovieService.isFavorite(principal.getName(), basicInfo.getId()) : false)
                .build();
    }

}
