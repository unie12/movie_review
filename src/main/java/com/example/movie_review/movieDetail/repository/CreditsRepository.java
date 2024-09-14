package com.example.movie_review.movieDetail.repository;

import com.example.movie_review.movieDetail.domain.Credits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditsRepository extends JpaRepository<Credits, Long> {
}
