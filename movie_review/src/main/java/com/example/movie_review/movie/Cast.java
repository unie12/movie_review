package com.example.movie_review.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cast")
public class Cast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cast_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_credits_id")
    private Credits movieCredits;

    private String a;
    private boolean adult;
    private Integer gender;
    private Integer tId;
    private String known_for_department;
    private String name;
    private String original_name;
    private double popularity;
    private String profile_path;
    private Integer cast_id;
    private String character;
    private String credit_id;
    private Integer order;




}
