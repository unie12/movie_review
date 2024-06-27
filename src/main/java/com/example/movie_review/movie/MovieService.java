package com.example.movie_review.movie;

import com.example.movie_review.domain.Genres;
import com.example.movie_review.movie.MovieRepository;
import com.example.movie_review.movie.Movies;
import com.example.movie_review.movie.RecommendationDto;
import com.example.movie_review.repository.GenresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenresRepository genresRepository;

    public ResponseEntity retrieveMovies(Pageable pageable) {
        Page<Movies> moviesPage = movieRepository.findAll(pageable);
        return new ResponseEntity<>(moviesPage, HttpStatus.OK);
    }

    public HashMap<Genres, Integer> sortByValue(HashMap<Genres, Integer> raw) {
        return raw.entrySet()
                .stream()
                .sorted((i1, i2) -> i1.getValue().compareTo(i2.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new
                ));
    }

    public HashMap<Genres, Integer> getPickedGenres(RecommendationDto recommendationDto) {
        HashMap<Genres, Integer> pickedGenres = new HashMap<>();
        recommendationDto.getPickedMovies().forEach(
                movieData -> {
                    Movies movie = movieRepository.findById(movieData.getMovieId()).orElseThrow(
                            () -> new IllegalStateException("Cannot find Movies with given id: " + movieData.getMovieId().toString()));
                    Set<Genres> genreList = movie.getGenres();
                    for(Genres g : genreList) {
                        Integer count = pickedGenres.getOrDefault(g, 0);
                        pickedGenres.put(g, count);
                    }
                }
        );
        return pickedGenres;
    }

    public Set<Genres> selectKeyInMap(HashMap<Genres, Integer> pickedGenresWithSort) {
        Iterator<Genres> keys = pickedGenresWithSort.keySet().iterator();
        Set<Genres> selectBestInKeys = new HashSet<>();
        int count = 0;
        while(keys.hasNext() && count < 2) {
            Genres genres = keys.next();
            selectBestInKeys.add(genres);
            count++;
        }

        return selectBestInKeys;
    }
}
