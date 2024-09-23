package com.example.movie_review.movieDetail.service;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.genre.Genres;
import com.example.movie_review.movieDetail.DTO.ActorDTO;
import com.example.movie_review.movieDetail.DTO.DirectorDTO;
import com.example.movie_review.movieDetail.DTO.MovieBasicInfo;
import com.example.movie_review.movieDetail.DTO.RecommendMovies;
import com.example.movie_review.movieDetail.domain.Crew;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.domain.MovieKeyword;
import com.example.movie_review.tmdb.TmdbService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieBasicService {
    private final DbMovieService dbMovieService;
    private final TmdbService tmdbService;
    private final MovieCommonDTOService movieCommonDTOService;
    private final ObjectMapper objectMapper;

    @Cacheable(value = "movieBasicInfo", key = "#movieTId")
    public MovieBasicInfo getMovieBasicInfo(Long movieTId) throws JsonProcessingException {
        DbMovies dbMovie = dbMovieService.findOrCreateMovie(movieTId);
        MovieDetails movieDetails = dbMovie.getMovieDetails();

        // 병렬로 API 호출
        Mono<String> providerMono = tmdbService.getMovieProvider(movieTId);
        Mono<String> videoMono = tmdbService.getYoutubeLink(movieTId);
        Mono<List<String>> imagesMono = tmdbService.getMovieImages(movieTId);
        Mono<List<MovieCommonDTO>> recommendationsMono = tmdbService.getMovieRecommendation(movieTId)
                .map(this::getRecommendMovies);

        Tuple4<String, String, List<String>, List<MovieCommonDTO>> results = Mono.zip(
                providerMono, videoMono, imagesMono, recommendationsMono
        ).block();


        String watchProviderUrl = extractWatchProviderUrl(results.getT1());
        String youtubeLink = results.getT2() != null ? "https://www.youtube.com/watch?v=" + results.getT2() : null;
        List<String> images = results.getT3();
        List<MovieCommonDTO> recommendedMovies = results.getT4();

        List<Crew> directors = dbMovieService.getDirectors(movieDetails);

        return MovieBasicInfo.builder()
                .id(dbMovie.getId())
                .tId(dbMovie.getTmdbId())
                .title(movieDetails.getTitle())
                .original_title(movieDetails.getOriginal_title())
                .backdrop_path(movieDetails.getBackdrop_path())
                .release_date(movieDetails.getRelease_date())
                .genres(movieDetails.getGenres().stream().map(Genres::getName).collect(Collectors.toList()))
                .runtime(movieDetails.getRuntime())
                .poster_path(movieDetails.getPoster_path())
                .poster_path(movieDetails.getPoster_path())
                .tagline(movieDetails.getTagline())
                .overview(movieDetails.getOverview())
                .directors(directors.stream().map(crew -> new DirectorDTO(crew.getId(), crew.getName(), crew.getProfile_path())).collect(Collectors.toList()))
                .actors(movieDetails.getCredits().getCast().stream()
                        .limit(24)
                        .map(cast -> new ActorDTO(cast.getId(), cast.getName(), cast.getProfile_path(), cast.getCharacter_name())).collect(Collectors.toList()))
                .watchProvider(watchProviderUrl)
                .tmdb_ratingAvg(movieDetails.getVote_average())
                .tmdb_ratingCnt(movieDetails.getVote_count())
                .recommendMovies(recommendedMovies)
                .youtubeLink(youtubeLink)
                .images(images)
                .keywords(movieDetails.getMovieKeywords().stream().map(keyword -> keyword.getKeyword().getName()).limit(20).collect(Collectors.toList()))
                .build();
    }

    private String extractWatchProviderUrl(String movieProvider) {
        try {
            JsonNode rootNode = objectMapper.readTree(movieProvider);
            return rootNode.path("results").path("KR").path("link").asText(null);
        } catch (Exception e) {
            log.error("Failed to extract watch provider URL", e);
            return null;
        }
    }

    private List<MovieCommonDTO> getRecommendMovies(String movieRecommendation) {
        try {
            if (movieRecommendation == null || movieRecommendation.isEmpty()) {
                throw new IllegalArgumentException("Empty or null Json Data");
            }
            RecommendMovies movies = objectMapper.readValue(movieRecommendation, RecommendMovies.class);
            return movies.getResults().stream().limit(18).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to parse recommended movies", e);
            return Collections.emptyList();
        }
    }

}
