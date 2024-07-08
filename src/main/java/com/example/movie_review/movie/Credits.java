package com.example.movie_review.movie;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credits")
public class Credits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credits_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "credits")
    private MovieDetails movieDetails;

    @OneToMany(mappedBy = "credits", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Crew> crew;

    @OneToMany(mappedBy = "credits", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cast> cast;
}