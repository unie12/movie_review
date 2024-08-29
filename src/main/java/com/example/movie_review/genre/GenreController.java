package com.example.movie_review.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genres")
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        List<GenreDTO> allGenres = genreService.getAllGenreDTO();
        return ResponseEntity.ok(allGenres);
    }
}
