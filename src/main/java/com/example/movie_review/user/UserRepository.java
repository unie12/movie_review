package com.example.movie_review.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(String email);

//    User findByUserName(String username);
//    Long countAllByUserRole(UserRole userRole);
}