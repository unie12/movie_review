package com.example.movie_review.movieDetail;

import com.example.movie_review.cache.MovieBasicService;
import com.example.movie_review.dbMovie.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.favoriteMovie.UserFavoriteMovieService;
import com.example.movie_review.genre.Genres;
import com.example.movie_review.heart.HeartService;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.ReviewCommonDTO;
import com.example.movie_review.review.ReviewDTO;
import com.example.movie_review.review.ReviewService;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
public class MovieDetailDTOService {
    private final DbMovieService dbMovieService;
    private final UserFavoriteMovieService userFavoriteMovieService;
    private final ReviewService reviewService;
    private final HeartService heartService;
    private final DbRatingService dbRatingService;
    private final TmdbService tmdbService;

    private final MovieBasicService movieBasicService;

    public MovieDetailDTO getMovieDetailDTO(Long movieTId, Authentication principal) {
        MovieBasicInfo basicInfo = movieBasicService.getMovieBasicInfo(movieTId);

        DbMovies dbMovie = dbMovieService.findOrCreateMovie(movieTId);
        List<Review> reviews = reviewService.findReviewByDbMovies(dbMovie);
        List<ReviewDTO> reviewDTOS = reviews.stream()
                .map(review -> {
                    User user = review.getUser();
                    UserCommonDTO userCommonDTO = UserCommonDTO.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .nickname(user.getNickname())
                            .picture(user.getPicture())
                            .build();
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
