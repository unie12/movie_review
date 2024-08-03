package com.example.movie_review.auth;

import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.review.service.ReviewService;
import com.example.movie_review.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final ReviewService reviewService;
    private final DbRatingService dbRatingService;

    @GetMapping("/home/realtime-data")
    public ResponseEntity<RealTimeData> getRealTimeData() {
        Long userCount = userService.getUserCount();
        Long totalRatings = dbRatingService.getTotalRatings();
        Long totalReviews = reviewService.getTotalReviews();

        RealTimeData data = new RealTimeData(userCount, totalRatings, totalReviews);
        return ResponseEntity.ok(data);
    }


    /**
     * 후에 영화 리스트...
     */

}
