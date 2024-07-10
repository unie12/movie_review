package com.example.movie_review.rating;

import com.example.movie_review.movie.MovieRepository;
import com.example.movie_review.movie.Movies;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class RatingController {

    private final RatingRepository ratingRepository;
    private final RatingService ratingService;

    private final UserRepository userRepository;
        private final MovieRepository movieRepository;

        /***
         * rating 정보 보기
         */
        @GetMapping("/ratings")
        public ResponseEntity retrieveRatings(@PageableDefault(size=100) Pageable pageable) {
            return ratingService.retrieveRatings(pageable);
        }

}
