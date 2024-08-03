package com.example.movie_review.favoriteMovie;

import com.example.movie_review.dbMovie.DbMovieRepository;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserRepository;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserFavoriteMovieService {

    private final UserRepository userRepository;
    private final DbMovieRepository dbMovieRepository;
    private final UserFavoriteMovieRepository userFavoriteMovieRepository;

    private final UserService userService;
    private final DbMovieService dbMovieService;

    @Transactional
    public boolean toggleFavorite(String email, Long movieId, boolean favorite) {
        User user = userService.getUserByEmail(email);
        DbMovies movie = dbMovieService.getDbMovieById(movieId);

        if(favorite) {
            if(userFavoriteMovieRepository.findByUserAndDbMovie(user, movie).isEmpty()) {
                UserFavoriteMovie userFavoriteMovie = new UserFavoriteMovie();
                userFavoriteMovie.setUser(user);
                userFavoriteMovie.setDbMovie(movie);
                userFavoriteMovie.setFavoriteDate(LocalDateTime.now());
                userFavoriteMovieRepository.save(userFavoriteMovie);
            }
            return true;
        } else {
            userFavoriteMovieRepository.deleteByUserAndDbMovie(user, movie);
            return false;
        }

    }

    public boolean isFavorite(String email, Long movieId) {
        User user = userService.getUserByEmail(email);
        DbMovies movie = dbMovieService.getDbMovieById(movieId);

        return userFavoriteMovieRepository.findByUserAndDbMovie(user, movie).isPresent();
    }
}
