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

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheUpdateService {
    private final MovieCacheService movieCacheService;
    private final UserDTOService userDTOService;
    private final CacheManager cacheManager;

    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void updateDailyCache() {
        movieCacheService.updateDailyMovieCache();
        evictCache("dailyHomePage");
    }
    @Scheduled(cron = "0 0 9 * * MON", zone = "Asia/Seoul")
    public void updateWeeklyCache() {
        movieCacheService.updateWeeklyMovieCache();
        evictCache("weeklyHomePage");
    }

    @Scheduled(cron ="0 0 0 * * *", zone = "Asia/Seoul")
    public void movieInfoCache() {
        evictCache("movieBasicInfo");
        evictCache("movieProvider");
        evictCache("personDetails");
    }

    private void evictCache(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }

    @Cacheable(value = "dailyHomePage", key = "'dailyHomePage'")
    public DailyHomeDTO getDailyHomeData() {
        return new DailyHomeDTO(
                movieCacheService.getBoxOfficeDTO(MovieType.DBOM),
                movieCacheService.getTmdbMoviesDTO(MovieType.POPULAR).stream().limit(20).collect(Collectors.toList()),
                movieCacheService.getTmdbMoviesDTO(MovieType.TRENDING).stream().limit(20).collect(Collectors.toList())
        );
    }

    @Cacheable(value = "weeklyHomePage", key = "'weeklyHomePage'")
    public WeeklyHomeDTO getWeeklyHomeData() {
        return new WeeklyHomeDTO(
//                userDTOService.getWeeklyReviewUsers(),
//                userDTOService.getWeeklyRatingUsers(),
                movieCacheService.getBoxOfficeDTO(MovieType.WBOM)
        );
    }
}
