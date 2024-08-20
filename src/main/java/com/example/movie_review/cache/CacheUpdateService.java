package com.example.movie_review.cache;

import com.example.movie_review.auth.DailyHomeDTO;
import com.example.movie_review.auth.WeeklyHomeDTO;
import com.example.movie_review.dbMovie.MovieType;
import com.example.movie_review.dbMovie.service.MovieCacheService;
import com.example.movie_review.user.service.UserDTOService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheUpdateService {
    private final MovieCacheService movieCacheService;
    private final UserDTOService userDTOService;
    private final CacheManager cacheManager;

    @Scheduled(cron = "0 0 9 * * *")
    public void updateDailyCache() {
        movieCacheService.updateDailyMovieCache();
        evictCache("dailyHomePage");
    }
    @Scheduled(cron = "0 0 9 * * MON")
    public void updateWeeklyCache() {
        movieCacheService.updateWeeklyMovieCache();
        evictCache("weeklyHomepage");
    }

    private void evictCache(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }

    @Cacheable(value = "dailyHomePage", key = "'dailyHomePage'")
    public DailyHomeDTO getDailyHomeData() {
        return new DailyHomeDTO(
                movieCacheService.getBoxOfficeDTO(MovieType.DBOM),
                movieCacheService.getTmdbMoviesDTO(MovieType.POPULAR),
                movieCacheService.getTmdbMoviesDTO(MovieType.TRENDING)
        );
    }

    @Cacheable(value = "weeklyHomePage", key = "'weeklyHomePage'")
    public WeeklyHomeDTO getWeeklyHomeData() {
        return new WeeklyHomeDTO(
                userDTOService.getWeeklyReviewUsers(),
                userDTOService.getWeeklyRatingUsers(),
                movieCacheService.getBoxOfficeDTO(MovieType.WBOM)
        );
    }
}
