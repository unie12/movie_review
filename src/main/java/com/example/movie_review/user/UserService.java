package com.example.movie_review.user;

import com.example.movie_review.user.DTO.WeeklyUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * nickname 중복 체크
     */
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * userId 입력받아 user return
     */
    public User getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    public Optional<User> getOptUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Email"));
    }

    public Long getUserCount() {
        return userRepository.getAllUserCount();
    }

    public void updateUserRole(User user) {
        UserRole newRole;
        if(user.getReviews().size() >= 100 && user.getDbRatings().size() >= 150 && user.getHearts().size() >= 100) {
            newRole = UserRole.CHALLENGER;
        }
        else if(user.getReviews().size() >= 40 && user.getDbRatings().size() >= 60 && user.getHearts().size() >= 40) {
            newRole = UserRole.MASTER;
        }
        else if(user.getReviews().size() >= 20 && user.getDbRatings().size() >= 30 && user.getHearts().size() >= 20) {
            newRole = UserRole.EMERALD;
        }
        else if(user.getReviews().size() >= 10 && user.getDbRatings().size() >= 15) {
            newRole = UserRole.SILVER;
        } else {
            newRole = UserRole.BRONZE;
        }

        if (user.getRole() != newRole) {
            user.updateRole(newRole);
            userRepository.save(user);
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateUserRoles() {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            updateUserRole(user);
        }
    }
}
