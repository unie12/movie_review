package com.example.movie_review.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {

        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "movieBasicInfo",
                "movieProvider",
                "personDetails",
                "dailyHomePage",
                "weeklyHomePage"
        );
        cacheManager.setAsyncCacheMode(true); // 비동기 처리
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100) // 초기 용량 100개 항목
                .maximumSize(1000) // 최대 1000개 항목 저장
                .expireAfterWrite(23, TimeUnit.HOURS) // 작성 후 23시간 후 만료
                .recordStats(); // 캐시 통계 기록
    }
}
