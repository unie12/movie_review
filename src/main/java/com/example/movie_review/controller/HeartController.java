package com.example.movie_review.controller;

import com.example.movie_review.service.HeartService;
import com.example.movie_review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;
    private final ReviewService reviewService;

//    @GetMapping("/add/{reviewId}")
//    @ResponseBody
//    public Map<String, Integer> addHeart(@PathVariable Long reviewId, Authentication auth) {
//        heartService.addHeart(auth.getName(), reviewId);
//        Review review = reviewService.findOne(reviewId);
//        Map<String, Integer> response = new HashMap<>();
//        response.put("heartCnt", review.getHeartCnt());
//        return response;
//    }
//
//
//    @GetMapping("/delete/{reviewId}")
//    public String deleteHeart(@PathVariable Long reviewId, Authentication auth) {
//        heartService.deleteHeart(auth.getName(), reviewId);
//        return "redirect:/viewReview/" + reviewId;
//    }

}
