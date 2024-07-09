package com.example.movie_review.dbMovie;

import com.example.movie_review.genre.Genres;
import com.example.movie_review.genre.GenresRepository;
import com.example.movie_review.movie.Crew;
import com.example.movie_review.movie.MovieDetailRepository;
import com.example.movie_review.movie.MovieDetails;
import com.example.movie_review.tmdb.TmdbService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DbMovieService {

    private final DbMovieRepository dbMovieRepository;
    private final MovieDetailRepository movieDetailRepository;
    private final GenresRepository genreRepository;
    private final TmdbService tmdbService;
    private final ObjectMapper objectMapper;

    public DbMovies findOrCreateMovie(Long movieId) {
        return dbMovieRepository.findByTmdbId(movieId)
            .orElseGet(() -> createMovieFromTmdb(movieId));
    }

    private DbMovies createMovieFromTmdb(Long movieId) {
        String movieDetailsJson = tmdbService.getMovieDetails(movieId).block();
        try {
            MovieDetails movieDetails = objectMapper.readValue(movieDetailsJson, MovieDetails.class);
            movieDetails.setTId(movieId.intValue());

            // 장르 매칭 로직
            List<Genres> matchedGenres = new ArrayList<>();
            for (Genres genre : movieDetails.getGenres()) {
                Genres matchedGenre = genreRepository.findByName(genre.getName())
                        .orElseGet(() -> genreRepository.save(new Genres(genre.getName())));
                matchedGenres.add(matchedGenre);
            }
            movieDetails.setGenres(matchedGenres);

            movieDetails = movieDetailRepository.save(movieDetails);

            DbMovies dbMovie = new DbMovies();
            dbMovie.setTmdbId(movieId);
            dbMovie.setMovieDetails(movieDetails);

            // 양방향 관계 설정?..
            movieDetails.setDbMovie(dbMovie);
            dbMovie = dbMovieRepository.save(dbMovie);

            return dbMovie;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing movie details from TMDB", e);
        }
    }

    public List<Crew> getDirectors(MovieDetails movieDetails) {
        if (movieDetails.getCredits() != null && movieDetails.getCredits().getCrew() != null) {
            return movieDetails.getCredits().getCrew().stream()
                    .filter(crew -> "Director".equals(crew.getJob()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
