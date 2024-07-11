package com.example.movie_review.dbRating;

import com.example.movie_review.dbMovie.DbMovieRepository;
import com.example.movie_review.dbMovie.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserRepository;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.movie_review.dbRating.QDbRatings.dbRatings;

@Service
@RequiredArgsConstructor
public class DbRatingService {
    private final UserRepository userRepository;
    private final DbMovieRepository dbMovieRepository;
    private final DbRatingRepository dbRatingRepository;

    private final UserService userService;
    private final DbMovieService dbMovieService;


    public DbRatings saveOrUpdateRating(Long movieId, String email, Double rating) {
        User user = userService.getUserByEmail(email);
        DbMovies dbMovies = dbMovieService.getDbMovieById(movieId);
        Optional<DbRatings> byDbMoviesAndUser = dbRatingRepository.findByDbMoviesAndUser(dbMovies, user);

        if(byDbMoviesAndUser.isPresent()) {
            DbRatings dbRatings = byDbMoviesAndUser.get();
            dbRatings.setScore(rating);
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
        DbRatings dbRatings = dbRatingRepository.findByDbMoviesAndUser(dbMovie, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user dbMovie"));

        dbRatingRepository.delete(dbRatings);
    }
}

