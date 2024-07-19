package com.example.movie_review.movieDetail;

import com.example.movie_review.dbMovie.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.favoriteMovie.UserFavoriteMovieService;
import com.example.movie_review.genre.Genres;
import com.example.movie_review.heart.HeartService;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.ReviewCommonDTO;
import com.example.movie_review.review.ReviewDTO;
import com.example.movie_review.review.ReviewService;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    public MovieDetailDTO getMovieDetailDTO(Long movieTId, Authentication principal) {
        DbMovies dbMovie = dbMovieService.findOrCreateMovie(movieTId);
        MovieDetails movieDetails = dbMovie.getMovieDetails();
        List<Crew> directors = dbMovieService.getDirectors(movieDetails);
        List<Review> reviews = reviewService.findReviewByDbMovies(dbMovie);

        List<ReviewDTO> reviewDTOS = reviews.stream()
                .map(review -> {
                    User user = review.getUser();
                    UserCommonDTO userCommonDTO = UserCommonDTO.builder()
                            .email(user.getEmail())
                            .nickname(user.getNickname())
                            .picture(user.getPicture())
                            .build();
                    ReviewCommonDTO reviewCommonDTO = ReviewCommonDTO.builder()
                            .id(review.getId())
                            .text(review.getContext())
                            .build();
                    return new ReviewDTO(review.getDbMovies().getDbRatingAvg(), review.getHeartCount(),userCommonDTO, reviewCommonDTO);
                })
                .collect(Collectors.toList());

        MovieDetailDTO.MovieDetailDTOBuilder movieDetailDTO = MovieDetailDTO.builder()
                .id(dbMovie.getId())
                .reviewCnt(dbMovie.getReviewCount())
                .title(movieDetails.getTitle())
                .original_title(movieDetails.getOriginal_title())
                .backdrop_path(movieDetails.getBackdrop_path())
                .release_date(movieDetails.getRelease_date())
                .genres(movieDetails.getGenres().stream().map(Genres::getName).collect(Collectors.toList()))
                .runtime(movieDetails.getRuntime())
                .poster_path(movieDetails.getPoster_path())
                .poster_path(movieDetails.getPoster_path())
                .tmdb_ratingAvg(movieDetails.getVote_average())
                .tmdb_ratingCnt(movieDetails.getVote_count())
                .ajou_ratingAvg(dbMovie.getDbRatingAvg())
                .ajou_ratingCnt(dbMovie.getDbRatingCount())
                .tagline(movieDetails.getTagline())
                .overview(movieDetails.getOverview())
                .directors(directors.stream().map(crew -> new DirectorDTO(crew.getId(), crew.getName(), crew.getProfile_path())).collect(Collectors.toList()))
                .actors(movieDetails.getCredits().getCast().stream()
                        .limit(24)
                        .map(cast -> new ActorDTO(cast.getId(), cast.getName(), cast.getProfile_path(), cast.getCharacter_name())).collect(Collectors.toList()))
//                .reviews(reviewService.getReviewDTOs(reviews))
                .reviews(reviewDTOS)
                .userHearts(heartService.getUserHearts(principal.getName()));

        if (principal != null) {
            String email = principal.getName();
            movieDetailDTO.isFavorite(userFavoriteMovieService.isFavorite(email, movieDetails.getId()));
        } else {
            movieDetailDTO.isFavorite(false);
        }

        return movieDetailDTO.build();
    }
}
