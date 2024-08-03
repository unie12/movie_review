package com.example.movie_review.tag;

import com.example.movie_review.movieLens.Movies;
import com.example.movie_review.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "tags")
    public class Tags {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "tag_id")
        private Long id;

        @Column(name = "movie_tag")
        private String tag;

        @Column(name = "timestamp")
        private Long timestamp;

        @Column(name = "user_id", nullable = false)
        private Long userId;

        @Column(name = "movie_id", nullable = false)
        private Long movieId;

        //    @Transient
        @ManyToOne
        @JoinColumn(name = "movie_id", insertable = false, updatable = false)
        private Movies movie;

        //    @Transient
        @ManyToOne
        @JoinColumn(name = "user_id", insertable = false, updatable = false)
        private User user;

    }
