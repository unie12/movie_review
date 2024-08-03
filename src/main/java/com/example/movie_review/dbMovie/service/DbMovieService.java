package com.example.movie_review.dbMovie.service;

import com.example.movie_review.dbMovie.repository.DbMovieRepository;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.genre.Genres;
import com.example.movie_review.genre.GenresRepository;
import com.example.movie_review.movieDetail.DTO.GenreDto;
import com.example.movie_review.movieDetail.domain.Cast;
import com.example.movie_review.movieDetail.domain.Credits;
import com.example.movie_review.movieDetail.domain.Crew;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.repository.MovieDetailRepository;
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
//@Transactional
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

    @Transactional
    private DbMovies createMovieFromTmdb(Long movieId) {
        String movieDetailsJson = tmdbService.getMovieDetails(movieId).block();
        try {
            MovieDetails movieDetails = objectMapper.readValue(movieDetailsJson, MovieDetails.class);
            movieDetails.setTId(movieId.intValue());

            // 장르 매칭 로직
            if (movieDetails.getGenreDtos() != null) {
                for (GenreDto genreDto : movieDetails.getGenreDtos()) {
                    Genres genre = genreRepository.findById(genreDto.getId())
                            .orElseThrow(() -> new RuntimeException("Genre not found: " + genreDto.getId()));
                    movieDetails.getGenres().add(genre);
                }

            }

            Credits credits = movieDetails.getCredits();
            if (credits != null) {
                for (Cast cast : credits.getCast()) {
                    cast.setCredits(credits);
                }
                for (Crew crew : credits.getCrew()) {
                    crew.setCredits(credits);
                }
                credits.setMovieDetails(movieDetails);
            }

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

//    @Transactional(readOnly = true)
    public DbMovies getDbMovieById(Long movieId) {
        return dbMovieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Movie ID"));
    }
}
