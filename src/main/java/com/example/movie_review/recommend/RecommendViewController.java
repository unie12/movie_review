package com.example.movie_review.recommend;

import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.movieDetail.service.MovieDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendViewController {
    private final MovieCommonDTOService movieCommonDTOService;
    private final MovieDetailService movieDetailService;

    @GetMapping
    public String listRecommend(Model model) {
        return "recommend";
    }

}
