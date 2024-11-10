package com.example.movie_review.movieDetail.service;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.repository.MovieDetailRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieDetailService {
    private static final int CACHE_SIZE = 72;

    private final MovieDetailRepository movieDetailRepository;
    private final CacheManager cacheManager;
    private final MovieCommonDTOService movieCommonDTOService;

    private static final String RANDOM_MOVIES_CACHE = "randomMoviesCache";
    private static final int PAGE_SIZE = 20;


    @PostConstruct
    public void initializeMovieCache() {
        try {
            List<MovieCommonDTO> allMovies = fetchAllQualifiedMovies();
            if (!allMovies.isEmpty()) {
                Collections.shuffle(allMovies);
                Cache cache = cacheManager.getCache(RANDOM_MOVIES_CACHE);
                if (cache != null) {
                    cache.put("movies", allMovies);
                }
            }
        } catch (Exception e) {
            // 초기화 실패 시 로그만 남기고 계속 진행
            log.error("Failed to initialize movie cache: ", e);
        }
    }

    @CacheEvict(value = "randomMoviesCache", allEntries = true)
    @Scheduled(cron = "30 * * * * *") // 매일 자정에 캐시 갱신
    public void refreshMovieCache() {
        updateRandomMoviesCache();
    }

    private void updateRandomMoviesCache() {
        List<MovieCommonDTO> allMovies = fetchAllQualifiedMovies();
        Collections.shuffle(allMovies);
        Cache cache = cacheManager.getCache(RANDOM_MOVIES_CACHE);
        if (cache != null) {
            cache.put("movies", allMovies);
        }
    }

    private List<MovieCommonDTO> fetchAllQualifiedMovies() {
        Long minVoteCount = 1000L;
        // HashMap을 사용하여 영화 ID 기반으로 중복 제거
        Map<Long, MovieCommonDTO> uniqueMoviesMap = new HashMap<>();

        List<Long> mainGenreIds = Arrays.asList(12L, 14L, 16L, 18L, 27L, 28L, 35L, 36L,
                37L, 53L, 80L, 99L, 878L, 9648L, 10402L, 10749L, 10751L, 10752L, 10770L);

        for (Long genreId : mainGenreIds) {
            try {
                List<MovieDetails> genreMovies = movieDetailRepository.findByGenreId(
                        genreId,
                        minVoteCount,
                        Pageable.unpaged()
                );

                for (MovieDetails movie : genreMovies) {
                    if (movie.getDbMovie() != null) {
                        MovieCommonDTO dto = movieCommonDTOService.getMovieCommonDTO(
                                movie.getDbMovie(),
                                movie.getDbMovie().getMovieDetails()
                        );
                        if (dto != null) {
                            // 영화 ID를 키로 사용하여 중복 제거
                            uniqueMoviesMap.put(movie.getDbMovie().getTmdbId(), dto);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error fetching movies for genre {}: ", genreId, e);
            }
        }

        return new ArrayList<>(uniqueMoviesMap.values());
    }

    public List<String> getAllMoviePosters() {
        List<MovieDetails> all = movieDetailRepository.findAll();
        List<String> posters = all.stream()
                .map(MovieDetails::getPoster_path)
                .collect(Collectors.toList());
        Collections.shuffle(posters);
        return posters.stream().limit(CACHE_SIZE).collect(Collectors.toList());
    }
    public List<MovieCommonDTO> getRandomMovies(int page) {
        Cache cache = cacheManager.getCache(RANDOM_MOVIES_CACHE);
        List<MovieCommonDTO> cachedMovies = null;

        if (cache != null) {
            @SuppressWarnings("unchecked")
            List<MovieCommonDTO> movies = cache.get("movies", List.class);
            cachedMovies = movies;
        }

        // 캐시가 없으면 새로 생성
        if (cachedMovies == null) {
            cachedMovies = fetchAllQualifiedMovies();
            if (!cachedMovies.isEmpty() && cache != null) {
                Collections.shuffle(cachedMovies);
                cache.put("movies", cachedMovies);
            }
        }

        if (cachedMovies != null && !cachedMovies.isEmpty()) {
            int start = page * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, cachedMovies.size());

            if (start < cachedMovies.size()) {
                return cachedMovies.subList(start, end);
            }
        }

        return Collections.emptyList();
    }

    public boolean hasMoreMovies(int page) {
        Cache cache = cacheManager.getCache(RANDOM_MOVIES_CACHE);
        if (cache != null) {
            @SuppressWarnings("unchecked")
            List<MovieCommonDTO> cachedMovies = cache.get("movies", List.class);
            if (cachedMovies != null) {
                return (page + 1) * PAGE_SIZE < cachedMovies.size();
            }
        }
        return false;
    }
    @Cacheable(value = "randomMoviePosters")
    public List<String> getRandomMoviePoster() {
        return getAllMoviePosters();
    }

    @Scheduled(fixedRate = 60 * 60 * 1000 * 2) // 120분
    public void refreshCachedPosters() {
        evictCache();
    }

    private void evictCache() {
        cacheManager.getCache("randomMoviePosters").clear();
    }

}
