package com.example.movie_review.rating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public ResponseEntity retrieveRatings(Pageable pageable) {
        Page<Ratings> ratingsPage = ratingRepository.findAll(pageable);
        return new ResponseEntity<>(ratingsPage, HttpStatus.OK);
    }
}
