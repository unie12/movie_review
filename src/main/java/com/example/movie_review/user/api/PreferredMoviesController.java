package com.example.movie_review.user.api;

import com.example.movie_review.user.domain.PreferredMovies;
import com.example.movie_review.user.service.PreferredMoviesService;
import com.example.movie_review.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferred-movies")
@RequiredArgsConstructor
public class PreferredMoviesController {
    private PreferredMoviesService preferredMoviesService;

    /**
     * 현재 사용자의 선호 영화 추가
     */
    @PostMapping
    public void addPreferredMovie(@RequestBody PreferredMovies preferredMovie) {
        preferredMoviesService.savePreferredMovie(preferredMovie);
    }

    /**
     * 현재 사용자의 선호 영화 리스트 가져오기
     */
    @GetMapping("/user/{userId}")
    public List<PreferredMovies> getPreferredMovies(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return preferredMoviesService.getPreferredMoviesByUser(user);
    }
}
