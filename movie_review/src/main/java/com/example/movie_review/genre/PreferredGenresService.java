package com.example.movie_review.genre;

import com.example.movie_review.movie.PreferredMovies;
import com.example.movie_review.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferredGenresService {
    private final PreferredGenresRepository preferredGenresRepository;

    public void savePreferredGenre(PreferredGenres preferredGenres) {
        User user = preferredGenres.getUser();
        Long genreId = preferredGenres.getGenreId();

        if(!preferredGenresRepository.existsByUserAndGenreId(user, genreId)) {
            System.out.println("Saving preferredGenres = " + preferredGenres);
            preferredGenresRepository.save(preferredGenres);
        }
        else {
            System.out.println("Already exists genre in list: " + preferredGenres);
        }
    }

    public List<PreferredGenres> getPreferredGenreByUser(User user) {
        return preferredGenresRepository.findByUser(user);
    }

    public List<PreferredGenres> findByUser(User user) {
        return preferredGenresRepository.findByUser(user);
    }

}
