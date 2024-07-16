package com.example.movie_review.movieDetail;


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

    @OneToOne(mappedBy = "credits", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MovieDetails movieDetails;

    @OneToMany(mappedBy = "credits", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Crew> crew;

    @OneToMany(mappedBy = "credits", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cast> cast;
}