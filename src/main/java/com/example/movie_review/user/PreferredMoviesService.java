package com.example.movie_review.user;

import com.example.movie_review.movieDetail.DTO.MovieDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            preferredMoviesRepository.save(preferredMovie);
        }
    }

    public List<PreferredMovies> getPreferredMoviesByUser(User user) {
        return preferredMoviesRepository.findByUser(user);
    }

    public List<PreferredMovies> findByUser(User user) {
        return preferredMoviesRepository.findByUser(user);
    }

    public void updatePreferredMovies(User user, List<MovieDTO> newFavoriteMovies) {
        Set<String> newMovieIds = newFavoriteMovies.stream()
                .map(MovieDTO::getId)
                .collect(Collectors.toSet());

        // 제거해야 할 영화 삭제
        user.getPreferredMovies().removeIf(movie -> !newMovieIds.contains(movie.getMovieId()));

        // 새로운 영화 추가 또는 기존 영화 업데이트
        for (MovieDTO movieDTO : newFavoriteMovies) {
            PreferredMovies preferredMovie = user.getPreferredMovies().stream()
                    .filter(movie -> movie.getMovieId().equals(movieDTO.getId()))
                    .findFirst()
                    .orElseGet(() -> {
                        PreferredMovies newMovie = new PreferredMovies();
                        newMovie.setMovieId(movieDTO.getId());
                        newMovie.setUser(user);
                        user.addPreferredMovie(newMovie);
                        return newMovie;
                    });

            preferredMovie.setMovieTitle(movieDTO.getTitle());
        }
    }
}
