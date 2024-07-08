package com.example.movie_review.movie;

import com.example.movie_review.genre.Genres;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieController {
    private final MovieRepository movieRepository;
    private final MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity retrieveMovies(@PageableDefault(size = 100) Pageable pageable) {
        return movieService.retrieveMovies(pageable);
    }

    @PostMapping("/recommendation")
    public List<Movies> getRecommendation(@RequestBody RecommendationDto recommendationDto) {
        HashMap<Genres, Integer> pickedGenres = movieService.getPickedGenres(recommendationDto);
        HashMap<Genres, Integer> pickedGenresWithSort = movieService.sortByValue(pickedGenres);
        Set<Genres> selectBestInKeys = movieService.selectKeyInMap(pickedGenresWithSort);
        return movieRepository.findByGenres(selectBestInKeys, recommendationDto);
    }
}
