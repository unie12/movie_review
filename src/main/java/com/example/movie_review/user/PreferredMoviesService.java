package com.example.movie_review.user;

import com.example.movie_review.movieDetail.MovieDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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

    public List<PreferredMovies> findByUser(User user) {
        return preferredMoviesRepository.findByUser(user);
    }

    public void updatePreferredMovies(User user, List<MovieDTO> newFavoriteMovies) {
        // 기존 선호 영화 제거
        user.getPreferredMovies().clear();

        // 새로운 선호 영화 추가
        if (newFavoriteMovies != null) {
            for (MovieDTO movieDTO : newFavoriteMovies) {
                PreferredMovies preferredMovie = new PreferredMovies();
                preferredMovie.setUser(user);
                preferredMovie.setMovieId(movieDTO.getId());
                preferredMovie.setMovieTitle(movieDTO.getTitle());
                user.addPreferredMovie(preferredMovie);
            }
        }
    }
}
