package com.example.movie_review.movieDetail.service;

import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;
import com.example.movie_review.dbMovie.repository.DbMovieRepository;
import com.example.movie_review.dbMovie.repository.DbMovieRepositoryCustom;
import com.example.movie_review.dbMovie.repository.DbMovieRepositoryImpl;
import com.example.movie_review.movieDetail.DTO.RecommendMovies;
import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.movieDetail.DTO.RecommendMovies;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.repository.MovieDetailRepository;
import com.example.movie_review.tmdb.TmdbService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieCommonDTOService {

    private final TmdbService tmdbService;
    private final ObjectMapper objectMapper;
    private final DbMovieService dbMovieService;

    private final MovieDetailRepository movieDetailRepository;
    private final DbMovieRepository dbMovieRepository;
    private final DbMovieRepositoryCustom dbMovieRepositoryCustom;

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

    public List<String> getMovieTitleSuggestions(String query) {
        return movieDetailRepository.findTop10ByTitleContainingIgnoreCase(query)
                .stream()
                .map(MovieDetails::getTitle)
                .collect(Collectors.toList());
    }

    public String getMoviePoster(Long tId) {
        return movieDetailRepository.findBytId(tId).getPoster_path();
    }

    /**
     * 특정 시간 (일단 하루) 동안 아주대 사람들의 찜, 평가, 리뷰 수가 많은 영화 반환
     */
    public List<MoviePopularityDTO> getAjouPopularMovies() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(24);
        double minRating = 3.4;

        return dbMovieRepositoryCustom.findAjouPopularMovies(startDate, minRating);
    }
}
