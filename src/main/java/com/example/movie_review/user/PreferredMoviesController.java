package com.example.movie_review.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferred-movies")
@RequiredArgsConstructor
public class PreferredMoviesController {
    private PreferredMoviesService preferredMoviesService;

    @PostMapping
    public void addPreferredMovie(@RequestBody PreferredMovies preferredMovie) {
        preferredMoviesService.savePreferredMovie(preferredMovie);
    }

    @GetMapping("/user/{userId}")
    public List<PreferredMovies> getPreferredMovies(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return preferredMoviesService.getPreferredMoviesByUser(user);
    }
}
