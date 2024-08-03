package com.example.movie_review.genre;

import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            preferredGenresRepository.save(preferredGenres);
        }
    }

    public List<PreferredGenres> getPreferredGenreByUser(User user) {
        return preferredGenresRepository.findByUser(user);
    }

    public List<PreferredGenres> findByUser(User user) {
        return preferredGenresRepository.findByUser(user);
    }


    public void updatePreferredGenres(User user, List<Long> newGenreIds) {
        Set<Long> existingGenreIds = user.getPreferredGenres().stream()
                .map(PreferredGenres::getGenreId)
                .collect(Collectors.toSet());

        // 삭제해야 할 장르 처리
        user.getPreferredGenres().removeIf(pg -> !newGenreIds.contains(pg.getGenreId()));

        // 새로 추가해야 할 장르 처리
        for (Long genreId : newGenreIds) {
            if (!existingGenreIds.contains(genreId)) {
                Genres genre = genresRepository.findById(genreId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid genre ID: " + genreId));
                PreferredGenres preferredGenre = new PreferredGenres(user, genre);
                user.addPreferredGenre(preferredGenre);
            }
        }
    }
}
