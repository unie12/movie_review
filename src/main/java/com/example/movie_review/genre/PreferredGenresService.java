package com.example.movie_review.genre;

import com.example.movie_review.user.DTO.UserProfileUpdateRequest;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserRepository;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferredGenresService {
    private final PreferredGenresRepository preferredGenresRepository;
    private final UserRepository userRepository;
    private final GenreService genreService;
    private final GenresRepository genresRepository;

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


    public void updatePreferredGenres(User user, List<Long> newGenreIds) {
        // 기존 선호 장르 제거
        user.getPreferredGenres().clear();

        // 새로운 선호 장르 추가
        if (newGenreIds != null) {
            for (Long genreId : newGenreIds) {
                Genres genre = genresRepository.findById(genreId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid genre ID: " + genreId));
                user.addPreferredGenre(new PreferredGenres(user, genre));
            }
        }
    }
}
