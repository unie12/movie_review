package com.example.movie_review.movieDetail.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
//@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cast")
public class Cast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "castId")
    private Long castId;

    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credits_id")
    private Credits credits;

    private boolean adult;
    private Integer gender;
    private String known_for_department;
    private String name;
    private String original_name;
    private double popularity;
    private String profile_path;
//    private Integer cast_original_id;
    @JsonProperty("character")
    private String character_name;
    private String credit_id;
    private Integer order_number;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cast)) return false;
        Cast cast = (Cast) o;
        return Objects.equals(id, cast.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}