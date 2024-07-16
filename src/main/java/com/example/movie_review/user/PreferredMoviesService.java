package com.example.movie_review.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferredMoviesService {
    private final PreferredMoviesRepository preferredMoviesRepository;
    private final UserRepository userRepository;
    public void savePreferredMovie(PreferredMovies preferredMovie) {
        User user = preferredMovie.getUser();
        String movieId = preferredMovie.getMovieId();

        if(!preferredMoviesRepository.existsByUserAndMovieId(user, movieId)) {
            System.out.println("Saving preferred movie: " + preferredMovie);
            preferredMoviesRepository.save(preferredMovie);
        }
        else {
            System.out.println("Already exists in list: " + preferredMovie);
        }
    }

    public List<PreferredMovies> getPreferredMoviesByUser(User user) {
        return preferredMoviesRepository.findByUser(user);
    }

    public List<PreferredMovies>findByUser(User user) {
        return preferredMoviesRepository.findByUser(user);
    }

}
