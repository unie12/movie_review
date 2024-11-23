package com.example.movie_review.anonymous;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/anonymous/stats")
public class AnonymousStatsController {

    private final AnonymousRatingService anonymousRatingService;

    @GetMapping("/ratings")
    public ResponseEntity<Map<String, Object>> getRatingStats() {
        return ResponseEntity.ok(anonymousRatingService.getRatingStats());
    }

    @GetMapping("/recommendations")
    public ResponseEntity<Map<String, Object>> getRecommendationsStats() {
        return ResponseEntity.ok(anonymousRatingService.getRecommendationStats());
    }
}
