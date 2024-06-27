package com.example.movie_review.service;

import com.example.movie_review.domain.Genres;
import com.example.movie_review.repository.GenresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenresRepository genresRepository;

    public Genres findOrCreateNew(String name) {
        return genresRepository.findByName(name).orElseGet(
                () -> genresRepository.save(new Genres(name))
        );
    }
}
