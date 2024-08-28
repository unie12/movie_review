package com.example.movie_review.user.service;

import com.example.movie_review.auth.RealTimeData;
import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.review.service.ReviewService;
import com.example.movie_review.user.DTO.WeeklyUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RealTimeDataService {
    private final RedisTemplate<String, RealTimeData> redisTemplate;
    private final UserService userService;
    private final DbRatingService dbRatingService;
    private final ReviewService reviewService;
    private final UserDTOService userDTOService;

    private static final String REAL_TIME_DATA_KEY = "realTimeData";
    private static final long CACHE_EXPIRATION = 60; // 60 seconds

    public RealTimeData getRealTimeData() {
        RealTimeData cachedData = redisTemplate.opsForValue().get(REAL_TIME_DATA_KEY);

        if (cachedData != null) {
            return cachedData;
        }

        RealTimeData freshData = fetchFreshData();
        redisTemplate.opsForValue().set(REAL_TIME_DATA_KEY, freshData, CACHE_EXPIRATION, TimeUnit.SECONDS);

        return freshData;
    }

    private RealTimeData fetchFreshData() {
        Long userCount = userService.getUserCount();
        Long totalRatings = dbRatingService.getTotalRatings();
        Long totalReviews = reviewService.getTotalReviews();
        Map<Double, Long> ratings = dbRatingService.getTotalRatingsDistribution();
        List<WeeklyUserDTO> weeklyRatingKing = userDTOService.getWeeklyRatingUsers();
        List<WeeklyUserDTO> weeklyReviewKing = userDTOService.getWeeklyReviewUsers();

        return new RealTimeData(userCount, totalRatings, totalReviews, ratings, weeklyRatingKing, weeklyReviewKing, userDTOService.getNextMondayNineAM());
    }

    public void invalidateCache() {
        redisTemplate.delete(REAL_TIME_DATA_KEY);
    }
}
