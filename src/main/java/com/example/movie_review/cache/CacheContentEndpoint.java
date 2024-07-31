package com.example.movie_review.cache;

import jakarta.websocket.OnClose;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

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
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof ConcurrentMapCache) {
                ConcurrentMap<Object, Object> nativeCache = ((ConcurrentMapCache) cache).getNativeCache();
                result.put(cacheName, new HashMap<>(nativeCache));
            }
        }
        return result;
    }
}
