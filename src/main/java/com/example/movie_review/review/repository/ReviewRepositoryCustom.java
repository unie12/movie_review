package com.example.movie_review.review.repository;

import com.example.movie_review.review.DTO.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ReviewRepositoryCustom {
    Page<ReviewDTO> findReviewByDbMoviesId(Long movieId, Pageable pageable, String sortBy, Sort.Direction direction);
}
