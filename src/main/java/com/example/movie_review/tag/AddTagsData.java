package com.example.movie_review.tag;

import com.example.movie_review.movie.MovieRepository;
import com.example.movie_review.movie.Movies;
import com.example.movie_review.rating.RatingRepository;
import com.example.movie_review.rating.Ratings;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// 해당 api 시작 전 데이터베이스에 모든 영화 정보를 기록하는 과정
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy")
public class AddTagsData {

    private final RatingRepository ratingRepo;
    private final UserRepository userRepo;
    private final MovieRepository movieRepo;
    private final TagRepository tagRepo;

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/add-tags")
    public ResponseEntity<?> addTags() throws IOException {
        File csv = new File("C:\\Users\\joeda\\Desktop\\spring\\movie_review\\ml-latest-small\\tags.csv");
        BufferedReader br = new BufferedReader(new BufferedReader(new FileReader(csv)));

        String line = "";
        boolean skipFirstLine = true;

        // 외래 키 무결성 체크 비활성화
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        while ((line = br.readLine()) != null) {
            if (skipFirstLine) {
                skipFirstLine = false;
                continue;
            }
            String[] token = line.split(",");

            Long userId = Long.parseLong(token[0]);
            Long movieId = Long.parseLong(token[1]);
            String tag = (token[2]);
            Long timestamp = Long.parseLong(token[3]);

            User user = userRepo.findById(userId).orElse(null);
            Movies movie = movieRepo.findById(movieId).orElse(null);

            tagRepo.save(Tags.builder()
                    .userId(userId)
                    .movieId(movieId)
                    .tag(tag)
                    .timestamp(timestamp)
                    .user(user)
                    .movie(movie)
                    .build());
        }

        // 외래 키 무결성 체크 활성화
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

        return ResponseEntity.ok().build();
    }
}