package com.example.movie_review.auth;

import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.review.ReviewService;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final ReviewService reviewService;
    private final DbRatingService dbRatingService;

    @GetMapping("/realtime-data")
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
