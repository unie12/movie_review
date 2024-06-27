package com.example.movie_review.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movies, Long>{
    @EntityGraph(value = "Movies.withGenres", type = EntityGraph.EntityGraphType.FETCH)
    Page<Movies> findAll(Pageable pageable);
}
