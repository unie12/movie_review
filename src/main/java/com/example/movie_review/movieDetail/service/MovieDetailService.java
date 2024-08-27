package com.example.movie_review.movieDetail.service;

import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.repository.MovieDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieDetailService {
    private static final int CACHE_SIZE = 60;

    private final MovieDetailRepository movieDetailRepository;
    private List<String> cachedPosters;

    @Scheduled(fixedRate = 60 * 60 * 1000) // 60분
    public void refreshCachedPosters() {
        List<MovieDetails> all = movieDetailRepository.findAll();
        cachedPosters = all.stream()
                .map(MovieDetails::getPoster_path)
                .collect(Collectors.toList());
        Collections.shuffle(cachedPosters);
        cachedPosters = cachedPosters.stream().limit(CACHE_SIZE).collect(Collectors.toList());
    }

    public List<String> getRandomMoviePoster(int count) {
        if(cachedPosters == null || cachedPosters.isEmpty()) {
            refreshCachedPosters();
        }
        List<String> shuffledPosters = new ArrayList<>(cachedPosters);
        Collections.shuffle(shuffledPosters);
        return shuffledPosters.stream().limit(count).collect(Collectors.toList());
    }
}
