package com.example.movie_review.cache;

import com.example.movie_review.dbMovie.MovieCacheService;
import com.example.movie_review.user.service.UserDTOService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheUpdateService {
    private final MovieCacheService movieCacheService;
    private final UserDTOService userDTOService;
    private final CacheManager cacheManager;

    @Scheduled(cron = "0 0 1 * * *")
    public void updateDailyCache() {
        movieCacheService.updateDailyMovieCache();
        evictCache("popularMovies");
        evictCache("trendingMovies");
        evictCache("dailyBoxOffice");
    }

    @Scheduled(cron = "0 0 1 * * MON")
    public void updateWeeklyCache() {
        movieCacheService.updateWeeklyMovieCache();
        evictCache("weeklyBoxOffice");
        evictCache("weeklyRatingUsers");
        evictCache("weeklyReviewUsers");

    }

    private void evictCache(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }
}
