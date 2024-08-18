package com.example.movie_review.user.repository;

import com.example.movie_review.review.Review;
import com.example.movie_review.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);

    User findByNickname(String nickname);

    @Query("SELECT COUNT(u) FROM User u")
    Long getAllUserCount();

    @Query("SELECT h.user FROM Heart h WHERE h.review = :review ORDER BY h.heartTime DESC")
    Page<User> findByLikedReviews(@Param("review") Review review, Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.nickname) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<User> searchByNickname(String query, Pageable pageable);

    List<User> findTop10ByNicknameContainingIgnoreCase(String nickname);

    List<User> findByMbti(String mbti);
}