package com.example.movie_review.movieDetail.service;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.genre.Genres;
import com.example.movie_review.movieDetail.DTO.ActorDTO;
import com.example.movie_review.movieDetail.DTO.DirectorDTO;
import com.example.movie_review.movieDetail.DTO.MovieBasicInfo;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieBasicService {
    private final DbMovieService dbMovieService;
    private final TmdbService tmdbService;
    private final MovieCommonDTOService movieCommonDTOService;


    @Cacheable(value = "movieBasicInfo", key = "#movieTId")
    public MovieBasicInfo getMovieBasicInfo(Long movieTId) throws JsonProcessingException {
        DbMovies dbMovie = dbMovieService.findOrCreateMovie(movieTId);
        MovieDetails movieDetails = dbMovie.getMovieDetails();
        List<Crew> directors = dbMovieService.getDirectors(movieDetails);
        String movieProvider = tmdbService.getMovieProvider(movieTId).block();

        // youtubeKey를 가져올 때 예외 처리
        String youtubeKey = null;
        String youtubeLink = null;
        try {
            youtubeKey = tmdbService.getYoutubeLink(movieTId).block();
            youtubeLink = youtubeKey != null ? "https://www.youtube.com/watch?v=" + youtubeKey : null;
        } catch (Exception e) {
            // 로그 출력 (선택사항)
            log.warn("Failed to fetch YouTube key for movie ID: " + movieTId, e);
        }

        String url = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(movieProvider);
            JsonNode resultsNode = rootNode.path("results");
            JsonNode krNode = resultsNode.path("KR");
            url = krNode.path("link").asText(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<MovieCommonDTO> recommendedMovies = movieCommonDTOService.getRecommendMovies(movieTId);
        List<String> images = tmdbService.getMovieImages(movieTId).block();

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
                .watchProvider(url)
                .tmdb_ratingAvg(movieDetails.getVote_average())
                .tmdb_ratingCnt(movieDetails.getVote_count())
                .recommendMovies(recommendedMovies)
                .youtubeLink(youtubeLink)
                .images(images)
                .keywords(movieDetails.getMovieKeywords().stream().map(keyword -> keyword.getKeyword().getName()).limit(20).collect(Collectors.toList()))
                .build();
    }

}
