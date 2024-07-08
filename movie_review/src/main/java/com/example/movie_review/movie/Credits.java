package com.example.movie_review.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credits")
public class Credits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "credits")
    private MovieDetails movieDetails;

    @OneToMany(mappedBy = "movieCredits")
    private List<Crew> crew;

    @OneToMany(mappedBy = "movieCredits")
    private List<Cast> cast;
}
