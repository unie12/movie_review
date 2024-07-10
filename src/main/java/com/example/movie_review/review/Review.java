package com.example.movie_review.review;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.Comment.Comment;
import com.example.movie_review.Heart.Heart;
import com.example.movie_review.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private LocalDateTime uploadDate = LocalDateTime.now(); // 글 작성일
    private String title; // 제목
    private String context; // 내용

    private String writer; // 닉네임
    private Double score; // 평점
    private Long viewCount; // 조회수

    /**
     * 매핑 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<Heart> hearts;
    private Integer heartCnt;


    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<Comment> comments;
    private Integer commentCnt;

    @ManyToOne
    @JoinColumn(name = "DbMovies_id")
    private DbMovies dbMovies;

}
