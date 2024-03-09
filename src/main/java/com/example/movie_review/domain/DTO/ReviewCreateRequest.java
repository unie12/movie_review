package com.example.movie_review.domain.DTO;

import com.example.movie_review.domain.User;
import com.example.movie_review.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ReviewCreateRequest {
    private Double score;
    private String title;
    private String context;
    private MultipartFile reviewImage;

    public Review toEntity(User user) {
        return Review.builder()
                .user(user)
                .title(title)
                .context(context)
                .commentCnt(0)
                .heartCnt(0)
                .score(score)
                .build();
    }

//    public Review toEntity(User user) {
//        return Review.builder()
//                .user(user)
//                .title(title)
//                .context(context)
//                .good(0L)
//                .commentCnt(0)
//                .score(score)
//                .build();
//    }

}
