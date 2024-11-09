package com.example.movie_review.recommend;

import com.example.movie_review.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RecommendRepository extends JpaRepository<RecommendedMovie, Long> {
    void deleteByUser(User user);

    List<RecommendedMovie> findByUserAndRecommendType(User user, RecommendType recommendType);
}
