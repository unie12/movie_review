package com.example.movie_review.user.repository;

import com.example.movie_review.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);

    User findByNickname(String nickname);

    @Query("SELECT COUNT(u) FROM User u")
    Long getAllUserCount();
}