package com.example.movie_review.service;

import com.example.movie_review.domain.DTO.JoinRequest;
import com.example.movie_review.domain.DTO.LoginRequest;
import com.example.movie_review.domain.User;
import com.example.movie_review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * loginId 중복 체크
     */
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    /**
     * nickname 중복 체크
     */
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 회원가입 기능 1
     * 화면에서 joinrequest 입력받아 user로 변환 후 저장
     */
    public void join(JoinRequest joinRequest) {
        userRepository.save(joinRequest.toEntity());
    }

    /**
     * 회원가입 기능 2
     * 1과 달리 비밀번호 암호화해서 저장
     */
    public void join2(JoinRequest joinRequest) {
        userRepository.save(joinRequest.toEntity(encoder.encode(joinRequest.getPassword())));
    }

    /**
     * 로그인 기능
     * 화면에서 loginrequet 입력받아 user return
     */
    public User login(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByLoginId(loginRequest.getLoginId());

        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if(!user.getPassword().equals(loginRequest.getPassword())) {
            return null;
        }

        return user;
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

    /**
     * loignId 입력받아 user return
     */
    public User getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    public User myInfo(String loginId) {
        return userRepository.findByLoginId(loginId).get();
    }
}
