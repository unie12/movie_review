package com.example.movie_review.genre;

import com.example.movie_review.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferredGenresRepository extends JpaRepository<PreferredGenres, Long> {

    List<PreferredGenres> findByUser(User user);

    boolean existsByUserAndGenreId(User user, Long genreId);

    void deleteByUser(User user);
}
