package com.example.movie_review.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "cachecontents")
@Slf4j
@RequiredArgsConstructor
public class CacheContentEndpoint {
    private final CacheManager cacheManager;

    @ReadOperation
    public Map<String ,Map<Object, Object>> cacheContents() {
        Map<String, Map<Object, Object>> result = new HashMap<>();
        for (String cacheName : cacheManager.getCacheNames()) {
            org.springframework.cache.Cache springCache = cacheManager.getCache(cacheName);
            if (springCache instanceof CaffeineCache) {
                Cache<Object, Object> nativeCache = ((CaffeineCache) springCache).getNativeCache();
                result.put(cacheName, new HashMap<>(nativeCache.asMap()));
            }
        }
        return result;
    }
}
