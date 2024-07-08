package com.example.movie_review.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "crew")
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credits_id")
    private Credits credits;

    private boolean adult;
    private Integer gender;
    private Integer tId;
    private String known_for_department;
    private String name;
    private String original_name;
    private double popularity;
    private String profile_path;
    private String credit_id;
    private String department;
    private String job;

}