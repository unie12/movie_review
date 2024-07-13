package com.example.movie_review.movie;

import com.example.movie_review.dbMovie.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.favoriteMovie.UserFavoriteMovieService;
import com.example.movie_review.genre.Genres;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieDetailDTOService {
    private final DbMovieService dbMovieService;
    private final UserFavoriteMovieService userFavoriteMovieService;

    public MovieDetailDTO getMovieDetailDTO(Long movieTId, OAuth2User principal) {
        DbMovies dbMovie = dbMovieService.findOrCreateMovie(movieTId);
        MovieDetails movieDetails = dbMovie.getMovieDetails();

        MovieDetailDTO.MovieDetailDTOBuilder movieDetailDTO = MovieDetailDTO.builder()
                .id(dbMovie.getId())
                .reviewCnt(dbMovie.getReviewCount())
                .ratingCnt(dbMovie.getDbRatingCount())
                .ratingAvg(dbMovie.getDbRatingAvg())
                .title(movieDetails.getTitle())
                .original_title(movieDetails.getOriginal_title())
                .backdrop_path(movieDetails.getBackdrop_path())
                .release_date(movieDetails.getRelease_date())
                .genres(movieDetails.getGenres().stream().map(Genres::getName).collect(Collectors.toList()))
                .runtime(movieDetails.getRuntime())
                .poster_path(movieDetails.getPoster_path());
//                .build();

        if (principal != null) {
            String email = principal.getAttribute("email");
            movieDetailDTO.isFavorite(userFavoriteMovieService.isFavorite(email, movieDetails.getId()));
        } else {
            movieDetailDTO.isFavorite(false);
        }

        return movieDetailDTO.build();
    }
}
