package com.example.movie_review.movieDetail.service;

import com.example.movie_review.movieDetail.DTO.RecommendMovies;
import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.movieDetail.DTO.RecommendMovies;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.tmdb.TmdbService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieCommonDTOService {

    private final TmdbService tmdbService;
    private final ObjectMapper objectMapper;
    private final DbMovieService dbMovieService;
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

    public List<MovieCommonDTO> getRecommendMovies(Long movieTId) throws JsonProcessingException {
        String movieRecommendation = tmdbService.getMovieRecommendation(movieTId).block();

        try {
            if(movieRecommendation == null || movieRecommendation.isEmpty()) {
                throw new Exception("Empty or null Json Data");
            }
            RecommendMovies movies = objectMapper.readValue(movieRecommendation, RecommendMovies.class);

            return movies.getResults().stream().limit(18)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
