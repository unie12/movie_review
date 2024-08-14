package com.example.movie_review.movieDetail.repository;

import com.example.movie_review.movieDetail.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByName(String keywordName);
}
