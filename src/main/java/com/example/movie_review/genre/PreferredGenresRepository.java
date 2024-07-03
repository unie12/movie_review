package com.example.movie_review.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferredGenresRepository extends JpaRepository<PreferredGenres, Long> {

}
