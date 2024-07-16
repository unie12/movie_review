package com.example.movie_review.tag;

import com.example.movie_review.movieLens.MovieRepository;
import com.example.movie_review.movieLens.Movies;
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
public class TagController {

    private final TagRepository tagRepository;
    private final TagService tagService;

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

        /***
         * tag 정보 보기
         */
        @GetMapping("/tags")
        public ResponseEntity retrieveTags(@PageableDefault(size=100) Pageable pageable) {
            return tagService.retrieveTags(pageable);
        }

        /***
         * 회원가입한 유저가 tag 할 시 저장
         */
        @PostMapping("/add-user-tag")
        public ResponseEntity<?> addUsertag(@RequestBody TagRequest tagRequest) {
            User user = userRepository.findById(tagRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            Movies movie = movieRepository.findById(tagRequest.getMovieId()).orElseThrow(() -> new RuntimeException("Movie not found"));
        
        Tags usertag = Tags.builder()
                .userId(tagRequest.getUserId())
                .movieId(tagRequest.getMovieId())
                .tag(tagRequest.getTag())
                .timestamp(System.currentTimeMillis())
                .user(user)
                .movie(movie)
                .build();
        
        tagRepository.save(usertag);
        return ResponseEntity.ok().build();
    }
}
