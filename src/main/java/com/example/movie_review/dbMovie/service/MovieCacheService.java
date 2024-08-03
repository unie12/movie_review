package com.example.movie_review.dbMovie.service;

import com.example.movie_review.dbMovie.MovieCache;
import com.example.movie_review.dbMovie.MovieCacheRepository;
import com.example.movie_review.dbMovie.MovieType;
import com.example.movie_review.kobis.BoxOfficeMovieDTO;
import com.example.movie_review.kobis.KobisService;
import com.example.movie_review.tmdb.TmdbService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
public class MovieCacheService {
    private final MovieCacheRepository movieCacheRepository;

    private final TmdbService tmdbService;
    private final KobisService kobisService;
    private final DbMovieService dbMovieService;

    private final ObjectMapper objectMapper;

    public void updateDailyMovieCache() {
        updateBoxOfficeCache(MovieType.DBOM);
        updateTmdbCache(MovieType.POPULAR);
        updateTmdbCache(MovieType.TRENDING);
    }

    public void updateWeeklyMovieCache() {
        updateBoxOfficeCache(MovieType.WBOM);
    }

    private void updateBoxOfficeCache(MovieType movieType) {
        try {
            String movieData;
            if (movieType == MovieType.DBOM) {
                movieData = getFormattedDBOM();
            } else if (movieType == MovieType.WBOM){
                movieData = getFormattedWBOM();
            } else {
                throw new IllegalArgumentException("Unsupported MovieType" + movieType);
            }

            MovieCache cache = movieCacheRepository.findByType(movieType)
                    .orElse(new MovieCache());
            cache.setType(movieType);
            cache.setMovieData(movieData);
            cache.setLastUpdated(LocalDateTime.now());
            movieCacheRepository.save(cache);
        } catch (Exception e) {
            log.error("Error updating " + movieType + " cache", e);
        }

    }

    private void updateTmdbCache(MovieType movieType) {
        try {
            String movieData;
            if (movieType == MovieType.POPULAR) {
                movieData = tmdbService.getPopularMovies().block();
            } else if (movieType == MovieType.TRENDING) {
                movieData = tmdbService.getTrendingMovies().block();
            } else {
                throw new IllegalArgumentException("Unsupported MovieType: " + movieType);
            }

            JsonNode moviesNode = objectMapper.readTree(movieData);
            for (JsonNode movieNode : moviesNode.path("results")) {
                Long tmdbId = movieNode.path("id").asLong();
                dbMovieService.findOrCreateMovie(tmdbId);
            }

            MovieCache cache = movieCacheRepository.findByType(movieType)
                    .orElse(new MovieCache());
            cache.setType(movieType);
            cache.setMovieData(movieData);
            cache.setLastUpdated(LocalDateTime.now());
            movieCacheRepository.save(cache);
        } catch (Exception e) {
            log.error("Error updating " + movieType + " cache", e);
        }
    }

    private String getFormattedDBOM() throws JsonProcessingException {
        JsonNode dailyBoxOffice = objectMapper.readTree(kobisService.getDailyBoxOfficeMovies().block());
        List<BoxOfficeMovieDTO> dailyBoxOfficeWithPosters = getBoxOfficeWithPosters(dailyBoxOffice, "dailyBoxOfficeList");
        return objectMapper.writeValueAsString(dailyBoxOfficeWithPosters);
    }

    private String getFormattedWBOM() throws JsonProcessingException {
        JsonNode weeklyBoxOffice = objectMapper.readTree(kobisService.getWeeklyBoxOfficeMovies().block());
        List<BoxOfficeMovieDTO> weeklyBoxOfficeWithPosters = getBoxOfficeWithPosters(weeklyBoxOffice, "weeklyBoxOfficeList");
        return objectMapper.writeValueAsString(weeklyBoxOfficeWithPosters);
    }

    private List<BoxOfficeMovieDTO> getBoxOfficeWithPosters(JsonNode boxOffice, String listType) {
        List<BoxOfficeMovieDTO> moviesWithPosters = new ArrayList<>();
        JsonNode movieList = boxOffice.path("boxOfficeResult").path(listType);

        for (JsonNode movie : movieList) {
            String title = movie.path("movieNm").asText();
            String audiAcc = movie.path("audiAcc").asText();
            String openDt = movie.path("openDt").asText();

            // TMDB에서 영화 검색
            JsonNode tmdbMovie = tmdbService.searchJsonMovieWithSim(title, openDt).block();
            String posterPath = null;
            String tmdbId = null;
            if (tmdbMovie != null) {
                posterPath = tmdbMovie.path("poster_path").asText();
                tmdbId = tmdbMovie.path("id").asText();
            }

            BoxOfficeMovieDTO movieDTO = new BoxOfficeMovieDTO(
                    title,
                    movie.path("rank").asText(),
                    posterPath,
                    tmdbId,
                    audiAcc
            );
            moviesWithPosters.add(movieDTO);
        }

        return moviesWithPosters;
    }

    @Cacheable(value = "popularMovies", key = "'popularMovies'")
    public String getPopularMovies() {
        return getMovieCache(MovieType.POPULAR);
    }

    @Cacheable(value = "trendingMovies", key = "'trendingMovies'")
    public String getTrendingMovies() {
        return getMovieCache(MovieType.TRENDING);
    }

    @Cacheable(value = "dailyBoxOffice", key = "'dailyBoxOffice'")
    public String getDailyBoxOffice() {
        return getMovieCache(MovieType.DBOM);
    }

    @Cacheable(value = "weeklyBoxOffice", key = "'weeklyBoxOffice'")
    public String getWeeklyBoxOffice() {
        return getMovieCache(MovieType.WBOM);
    }

    private String getMovieCache(MovieType movieType) {
        return movieCacheRepository.findByType(movieType)
                .map(MovieCache::getMovieData)
                .orElseThrow(() -> new RuntimeException(movieType + " cache not found"));
    }
}
