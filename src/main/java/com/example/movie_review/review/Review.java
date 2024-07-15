package com.example.movie_review.review;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.comment.Comment;
import com.example.movie_review.heart.Heart;
import com.example.movie_review.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class Review{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private LocalDateTime uploadDate = LocalDateTime.now(); // 글 작성일
    private String title; // 제목
    private String context; // 내용


    /**
     * 매핑 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>(); // 리뷰 좋아요
//    private Integer heartCount = 0;


    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<Comment> comments; // 리뷰에 달린 댓글

    @ManyToOne
    @JoinColumn(name = "DbMovies_id")
    private DbMovies dbMovies;

    public int getHeartCount() {
        return hearts.size();
    }

}
