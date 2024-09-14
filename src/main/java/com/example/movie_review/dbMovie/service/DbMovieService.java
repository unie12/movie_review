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
import com.example.movie_review.movieDetail.repository.CreditsRepository;
import com.example.movie_review.movieDetail.repository.MovieDetailRepository;
import com.example.movie_review.tmdb.TmdbService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DbMovieService {

    private final DbMovieRepository dbMovieRepository;
    private final MovieDetailRepository movieDetailRepository;
    private final GenresRepository genreRepository;
    private final CreditsRepository creditsRepository;
    private final TmdbService tmdbService;
    private final ObjectMapper objectMapper;

    @Transactional
    public DbMovies findOrCreateMovie(Long movieTId) {
        log.info("Finding or creating movie with tmdbId: {}", movieTId);
        return dbMovieRepository.findByTmdbId(movieTId)
                .orElseGet(() -> createMovieFromTmdb(movieTId));
    }

    @Transactional
    private DbMovies createMovieFromTmdb(Long movieTId) {
        return dbMovieRepository.findByTmdbId(movieTId).orElseGet(() -> {
            String movieDetailsJson = tmdbService.getMovieDetails(movieTId).block();
            MovieDetails movieDetails = null;

            try {
                movieDetails = objectMapper.readValue(movieDetailsJson, MovieDetails.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            System.out.println("movieDetails = " + movieDetails);
            System.out.println("movieDetails.getId() = " + movieDetails.getId());
            System.out.println("movieDetails.getTId() = " + movieDetails.getTId());
            System.out.println("movieDetails.getCredits() = " + movieDetails.getCredits());

            movieDetails.setTId(movieTId.intValue());
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
            dbMovie.setTmdbId(movieTId);
            dbMovie.setMovieDetails(movieDetails);
            movieDetails.setDbMovie(dbMovie);

            dbMovie = dbMovieRepository.save(dbMovie);

            tmdbService.addMovieKeywords(movieTId, movieDetails);

            return dbMovie;
        });
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public DbMovies createMovieFromTmdb(Long movieTId) {
//        log.info("Creating movie from TMDB with id: {}", movieTId);
//        return dbMovieRepository.findByTmdbId(movieTId).orElseGet(() -> {
//            MovieDetails movieDetails = createMovieDetails(movieTId);
//
//            DbMovies dbMovie = new DbMovies();
//            dbMovie.setTmdbId(movieTId);
//            dbMovie.setMovieDetails(movieDetails);
//            movieDetails.setDbMovie(dbMovie);
//
//            try {
//                return dbMovieRepository.saveAndFlush(dbMovie);
//            } catch (DataIntegrityViolationException e) {
//                log.warn("Concurrent creation detected for movieTId: {}. Retrying...", movieTId);
//                return dbMovieRepository.findByTmdbId(movieTId)
//                        .orElseThrow(() -> new RuntimeException("Failed to find or create movie after concurrent creation", e));
//            }
//        });
//    }
//
//    private MovieDetails createMovieDetails(Long movieTId) {
//        // 영화 정보를 가져오는 로직
//        String movieDetailsJson = tmdbService.getMovieDetails(movieTId).block();
//        MovieDetails movieDetails;
//        try {
//            movieDetails = objectMapper.readValue(movieDetailsJson, MovieDetails.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("movieDetails = " + movieDetails);
//        System.out.println("movieDetails.getId() = " + movieDetails.getId());
//        System.out.println("movieDetails.getTId() = " + movieDetails.getTId());
//
//        movieDetails.setTId(movieTId.intValue());
//        // 장르 매칭 로직
//        if (movieDetails.getGenreDtos() != null) {
//            for (GenreDto genreDto : movieDetails.getGenreDtos()) {
//                Genres genre = genreRepository.findById(genreDto.getId())
//                        .orElseThrow(() -> new RuntimeException("Genre not found: " + genreDto.getId()));
//                movieDetails.getGenres().add(genre);
//            }
//        }
//
//        Credits credits = movieDetails.getCredits();
//        if (credits != null) {
//            credits.setCast(credits.getCast().stream()
//                    .limit(30)
//                    .collect(Collectors.toList()));
//            for (Cast cast : credits.getCast()) {
//                cast.setCredits(credits);
//            }
//            credits.setCrew(credits.getCrew().stream()
//                    .filter(c -> "Director".equals(c.getJob()))
//                    .collect(Collectors.toList()));
//            for (Crew crew : credits.getCrew()) {
//                crew.setCredits(credits);
//            }
//            credits.setMovieDetails(movieDetails);
//            movieDetails.setCredits(credits);
//        }
//        return movieDetailRepository.save(movieDetails);
////        return movieDetails;
//    }


//    @Transactional
//    private DbMovies createMovieFromTmdb(Long movieTId) {
//        DbMovies existingMovie = dbMovieRepository.findByTmdbId(movieTId).orElse(null);
//        if (existingMovie != null) {
//            return existingMovie; // 이미 존재하면 즉시 반환
//        }
//
//        // 영화 정보를 가져오는 로직
//        String movieDetailsJson = tmdbService.getMovieDetails(movieTId).block();
//        MovieDetails movieDetails;
//        try {
//            movieDetails = objectMapper.readValue(movieDetailsJson, MovieDetails.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("movieDetails = " + movieDetails);
//        System.out.println("movieDetails.getId() = " + movieDetails.getId());
//        System.out.println("movieDetails.getTId() = " + movieDetails.getTId());
//        System.out.println("movieDetails.getCredits() = " + movieDetails.getCredits());
//
//        movieDetails.setTId(movieTId.intValue());
//        // 장르 매칭 로직
//        if (movieDetails.getGenreDtos() != null) {
//            for (GenreDto genreDto : movieDetails.getGenreDtos()) {
//                Genres genre = genreRepository.findById(genreDto.getId())
//                        .orElseThrow(() -> new RuntimeException("Genre not found: " + genreDto.getId()));
//                movieDetails.getGenres().add(genre);
//            }
//        }
//
//        Credits credits = movieDetails.getCredits();
//        if (credits != null) {
//            final Credits finalCredits = credits; // 새로운 final 변수 생성
//
//            credits.setCast(credits.getCast().stream()
//                    .limit(30)
//                    .map(cast -> {
//                        Cast managedCast = new Cast();
//                        managedCast.setId(null);
//                        BeanUtils.copyProperties(cast, managedCast, "id", "credits");
//                        managedCast.setCredits(finalCredits);
//                        return managedCast;
//                    })
//                    .collect(Collectors.toList()));
//
//            credits.setCrew(credits.getCrew().stream()
//                    .filter(c -> "Director".equals(c.getJob()))
//                    .map(crew -> {
//                        Crew managedCrew = new Crew();
//                        managedCrew.setId(null);
//                        BeanUtils.copyProperties(crew, managedCrew, "id", "credits");
//                        managedCrew.setCredits(finalCredits);
//                        return managedCrew;
//                    })
//                    .collect(Collectors.toList()));
//
//            credits.setMovieDetails(movieDetails);
//            movieDetails.setCredits(credits);
//
//            credits = creditsRepository.save(credits);
//        }
//        movieDetails = movieDetailRepository.save(movieDetails);
//
//        DbMovies dbMovie = new DbMovies();
//        dbMovie.setTmdbId(movieTId);
//        dbMovie.setMovieDetails(movieDetails);
//        movieDetails.setDbMovie(dbMovie);
//
//        dbMovie = dbMovieRepository.save(dbMovie);
//
//        tmdbService.addMovieKeywords(movieTId, movieDetails);
//
//        return dbMovie;
//    }

    private DbMovies retryFindOrCreateMovie(Long movieTId) {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                return dbMovieRepository.findByTmdbId(movieTId)
                        .orElseGet(() -> createMovieFromTmdb(movieTId));
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
