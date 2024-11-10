package com.example.movie_review.movieDetail.service;

import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;
import com.example.movie_review.dbMovie.repository.DbMovieRepository;
import com.example.movie_review.dbMovie.repository.DbMovieRepositoryCustom;
import com.example.movie_review.dbMovie.repository.DbMovieRepositoryImpl;
import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.movieDetail.DTO.RecommendMovies;
import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.movieDetail.DTO.RecommendMovies;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.repository.MovieDetailRepository;
import com.example.movie_review.recommend.RecommendedMovie;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final UserRepository userRepository;

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
     * 1시간마다 캐시 갱신
     */
    @Cacheable(value = "ajouPopularMovies", key = "'ajou_' + T(java.time.LocalDateTime).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyyMMddHH'))", unless = "#result.isEmpty()")
    public List<MoviePopularityDTO> getAjouPopularMovies() {
        List<Long> userIds = userRepository.findAll().stream()
                .map(User::getId)
                .toList();

        LocalDateTime startDate = LocalDateTime.now().minusHours(48);
        double minRating = 3.5;

        return dbMovieRepositoryCustom.findPopularMoviesByUserGroup(userIds, startDate, minRating);
    }

    @Cacheable(value = "ajouNotPopularMovies", key = "'ajou_' + T(java.time.LocalDateTime).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyyMMddHH'))", unless = "#result.isEmpty()")
    public List<MoviePopularityDTO> getAjouNotPopularMovies() {
        List<Long> userIds = userRepository.findAll().stream()
                .map(User::getId)
                .toList();

        LocalDateTime startDate = LocalDateTime.now().minusHours(48);
        double maxRating = 3.0;

        return dbMovieRepositoryCustom.findNotPopularMoviesByUserGroup(userIds, startDate, maxRating);
    }

    /**
     * 동일한 mbti 사용자의 선호 영화
     * 현재 rating만 가지고 판단
     */
    @Cacheable(value = "mbtiPopularMovies", key = "'mbti_' + #currentUser.getMbti() + '_' + T(java.time.LocalDateTime).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyyMMddHH'))", unless = "#result.isEmpty()")
    public List<MoviePopularityDTO> getSameMbtiMovies(User currentUser) {
        List<Long> userIds = userRepository.findByMbti(currentUser.getMbti()).stream()
//                .filter(user -> !user.getId().equals(currentUser.getId()))
                .map(User::getId)
                .collect(Collectors.toList());

        LocalDateTime startDate = LocalDateTime.now().minusHours(48);
        double minRating = 3.5;

        return dbMovieRepositoryCustom.findPopularMoviesByUserGroup(userIds, startDate, minRating);
    }

    /**
     * 동일한 학과 사용자의 선호 영화
     */
    @Cacheable(value = "departmentPopularMovies", key = "'dept_' + #currentUser.getDepartment() + '_' + T(java.time.LocalDateTime).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyyMMddHH'))", unless = "#result.isEmpty()")
    public List<MoviePopularityDTO> getSameDepartmentMovies(User currentUser) {
        List<Long> userIds = userRepository.findByDepartment(currentUser.getDepartment()).stream()
//                .filter(user -> !user.getId().equals(currentUser.getId()))
                .map(User::getId)
                .collect(Collectors.toList());

        LocalDateTime startDate = LocalDateTime.now().minusHours(48);
        double minRating = 3.5;

        return dbMovieRepositoryCustom.findPopularMoviesByUserGroup(userIds, startDate, minRating);
    }

}
