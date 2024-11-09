package com.example.movie_review.recommend;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.movieDetail.service.MovieDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendViewController {
    private final MovieCommonDTOService movieCommonDTOService;
    private final MovieDetailService movieDetailService;

    @GetMapping
    public String listRecommend(Model model) {
        List<MovieCommonDTO> initialMovies = movieDetailService.getRandomMovies(0);
        model.addAttribute("movies", initialMovies);
        return "recommend";
    }

}
