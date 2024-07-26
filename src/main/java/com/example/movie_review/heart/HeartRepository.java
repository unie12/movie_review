package com.example.movie_review.heart;

import com.example.movie_review.review.Review;
import com.example.movie_review.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByUserAndReview(User user, Review review);

    List<Heart> findByUser(User user);

    boolean existsByUserAndReview(User user, Review review);
}
