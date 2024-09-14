package com.example.movie_review.movieDetail.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
//@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credits")
public class Credits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credits_id")
    private Long id;

    @OneToOne(mappedBy = "credits")
    private MovieDetails movieDetails;

    @OneToMany(mappedBy = "credits", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Crew> crew;

    @OneToMany(mappedBy = "credits", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Cast> cast;
}