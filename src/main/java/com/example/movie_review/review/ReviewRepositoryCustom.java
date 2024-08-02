package com.example.movie_review.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ReviewRepositoryCustom {
    Page<ReviewDTO> findReviewByMovieId(Long movieId, Pageable pageable, String sortBy, Sort.Direction direction);
}
