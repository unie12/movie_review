package com.example.movie_review.dbRating;

import com.example.movie_review.dbMovie.repository.DbMovieRepository;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.repository.UserRepository;
import com.example.movie_review.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DbRatingService {
    private final UserRepository userRepository;
    private final DbMovieRepository dbMovieRepository;
    private final DbRatingRepository dbRatingRepository;

    private final UserService userService;
    private final DbMovieService dbMovieService;


    @Transactional
    public DbRatings saveOrUpdateRating(Long movieId, String email, Double rating) {
        User user = userService.getUserByEmail(email);
        DbMovies dbMovies = dbMovieService.getDbMovieById(movieId);
        Optional<DbRatings> byDbMoviesAndUser = dbRatingRepository.findByDbMoviesAndUser(dbMovies, user);

        if(byDbMoviesAndUser.isPresent()) {
            DbRatings dbRatings = byDbMoviesAndUser.get();
            dbRatings.setScore(rating);
            dbRatings.setUploadRating(LocalDateTime.now());
            return dbRatingRepository.save(dbRatings);
        }
        else {
            DbRatings dbRatings = new DbRatings();
            dbRatings.setUser(user);
            dbRatings.setDbMovies(dbMovies);
            dbRatings.setScore(rating);
            return dbRatingRepository.save(dbRatings);
        }
    }

    public Optional<DbRatings> getDbRating(String email, Long movieId) {
        User user = userService.getUserByEmail(email);
        DbMovies dbMovie = dbMovieService.getDbMovieById(movieId);

        return dbRatingRepository.findByDbMoviesAndUser(dbMovie, user);
    }

    public void deleteDbRating(Long movieId, String email) {
        User user = userService.getUserByEmail(email);
        DbMovies dbMovie = dbMovieService.getDbMovieById(movieId);
        dbRatingRepository.findByDbMoviesAndUser(dbMovie, user)
                .ifPresent(dbRatingRepository::delete);
    }


    public Double getUserRatingForMovie(Long userId, Long movieId) {
        return dbRatingRepository.findByUserIdAndDbMoviesId(userId, movieId)
                .map(DbRatings::getScore)
                .orElse(null);
    }

    public Long getTotalRatings() {
        return dbRatingRepository.getTotalRatings();
    }

    public Map<Double, Long> getTotalRatingsDistribution() {
        Map<Double, Long> ratingDistribution = new LinkedHashMap<>();
        for (double i = 0.5; i <= 5.0; i += 0.5) {
            ratingDistribution.put(i, 0L);
        }

        List<DbRatings> ratings  = dbRatingRepository.findAll();
        for(DbRatings rating : ratings) {
            Double score = rating.getScore();
            ratingDistribution.put(score, ratingDistribution.getOrDefault(score, 0L) + 1);
        }

        return ratingDistribution;
    }
}

