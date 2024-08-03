package com.example.movie_review.movieDetail.service;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.MovieCommonDTO;
import com.example.movie_review.movieDetail.MovieDetails;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieCommonDTOService {

    public MovieCommonDTO getMovieCommonDTO(DbMovies dbMovie, MovieDetails movieDetails) {
        MovieCommonDTO movieCommonDTO = MovieCommonDTO.builder()
                .id(dbMovie.getId())
                .tId(movieDetails.getTId())
                .title(movieDetails.getTitle())
                .poster_path(movieDetails.getPoster_path())
                .release_date(movieDetails.getRelease_date())
                .runtime(movieDetails.getRuntime())
                .ajou_rating(dbMovie.getDbRatingAvg())
                .ajou_rating_cnt(dbMovie.getDbRatingCount())
                .build();

        return movieCommonDTO;
    }
}
