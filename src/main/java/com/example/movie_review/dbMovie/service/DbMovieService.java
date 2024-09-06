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
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
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

    @Transactional
    public DbMovies findOrCreateMovie(Long movieId) {
        try {
            return dbMovieRepository.findByTmdbId(movieId)
                    .orElseGet(() -> createMovieFromTmdb(movieId));
        } catch (OptimisticLockException e) {
            // 버전 충돌 발생 시 재시도 로직
            return retryFindOrCreateMovie(movieId);
        }
    }
    private DbMovies retryFindOrCreateMovie(Long movieId) {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                return dbMovieRepository.findByTmdbId(movieId)
                        .orElseGet(() -> createMovieFromTmdb(movieId));
            } catch (OptimisticLockException e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw e;
                }
                // 잠시 대기 후 재시도
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new RuntimeException("Failed to find or create movie after multiple attempts");
    }

    private DbMovies createMovieFromTmdb(Long movieId) {
        return dbMovieRepository.findByTmdbId(movieId).orElseGet(() -> {
            String movieDetailsJson = tmdbService.getMovieDetails(movieId).block();
            MovieDetails movieDetails = null;

            try {
                movieDetails = objectMapper.readValue(movieDetailsJson, MovieDetails.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

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
                credits.setCast(credits.getCast().stream()
                        .limit(30)
                        .collect(Collectors.toList()));
                for (Cast cast : credits.getCast()) {
                    cast.setCredits(credits);
                }
                credits.setCrew(credits.getCrew().stream()
                        .filter(c -> "Director".equals(c.getJob()))
                        .collect(Collectors.toList()));
                for (Crew crew : credits.getCrew()) {
                    crew.setCredits(credits);
                }
                credits.setMovieDetails(movieDetails);
                movieDetails.setCredits(credits);
            }

            movieDetails = movieDetailRepository.save(movieDetails);

            DbMovies dbMovie = new DbMovies();
            dbMovie.setTmdbId(movieId);
            dbMovie.setMovieDetails(movieDetails);
            movieDetails.setDbMovie(dbMovie);

            dbMovie = dbMovieRepository.save(dbMovie);

            tmdbService.addMovieKeywords(movieId, movieDetails);

            return dbMovie;
        });
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
