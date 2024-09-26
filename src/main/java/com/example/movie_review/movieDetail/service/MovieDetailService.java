package com.example.movie_review.movieDetail.service;

import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.repository.MovieDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieDetailService {
    private static final int CACHE_SIZE = 72;

    private final MovieDetailRepository movieDetailRepository;
    private final CacheManager cacheManager;

    public List<String> getAllMoviePosters() {
        List<MovieDetails> all = movieDetailRepository.findAll();
        List<String> posters = all.stream()
                .map(MovieDetails::getPoster_path)
                .collect(Collectors.toList());
        Collections.shuffle(posters);
        return posters.stream().limit(CACHE_SIZE).collect(Collectors.toList());
    }

    @Cacheable(value = "randomMoviePosters", key = "#count")
    public List<String> getRandomMoviePoster(int count) {
        List<String> allPosters = getAllMoviePosters();
        List<String> shuffledPosters = new ArrayList<>(allPosters);
        Collections.shuffle(shuffledPosters);
        return shuffledPosters.stream().limit(count).collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 60 * 60 * 1000 * 2) // 120분
    public void refreshCachedPosters() {
        evictCache();
    }
    private void evictCache() {
        cacheManager.getCache("randomMoviePosters").clear();
    }



//    @Scheduled(fixedRate = 60 * 60 * 1000) // 60분
//    public void refreshCachedPosters() {
//        List<MovieDetails> all = movieDetailRepository.findAll();
//        cachedPosters = all.stream()
//                .map(MovieDetails::getPoster_path)
//                .collect(Collectors.toList());
//        Collections.shuffle(cachedPosters);
//        cachedPosters = cachedPosters.stream().limit(CACHE_SIZE).collect(Collectors.toList());
//    }
//
//    public List<String> getRandomMoviePoster(int count) {
//        if(cachedPosters == null || cachedPosters.isEmpty()) {
//            refreshCachedPosters();
//        }
//        List<String> shuffledPosters = new ArrayList<>(cachedPosters);
//        Collections.shuffle(shuffledPosters);
//        return shuffledPosters.stream().limit(count).collect(Collectors.toList());
//    }

//    @Cacheable(value = "randomMoviePosters", key = "'allPosters'")



}
