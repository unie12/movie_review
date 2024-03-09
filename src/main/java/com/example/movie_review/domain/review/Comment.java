package com.example.movie_review.domain.review;

import com.example.movie_review.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    /**
     * user가 작서항 댓글
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * 리뷰에 달린 댓글
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    public void update(String newBody) {
        this.body = newBody;
    }

}
