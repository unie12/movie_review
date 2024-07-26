package com.example.movie_review.review;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByDbMoviesAndUser(DbMovies dbMovies, User user);

    List<Review> findByUser(User user);

    List<Review> findReviewByDbMovies(DbMovies dbMovies);

    Page<Review> findByDbMovies_MovieDetails_Id(Long movieId, PageRequest pageRequest);

}
