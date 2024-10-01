package com.example.movie_review.recommend;

import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    private final UserService userService;

    @GetMapping("/content/{userEmail}")
    public ResponseEntity<List<MovieRecommendDTO>> getContentBasedRecommendations(@PathVariable String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        List<MovieRecommendDTO> recommendations = recommendService.getContentBasedRecommendation(user);
        return ResponseEntity.ok(recommendations);
    }

}
