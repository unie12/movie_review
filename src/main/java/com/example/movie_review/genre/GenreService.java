package com.example.movie_review.genre;

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
