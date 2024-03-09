package com.example.movie_review;

import com.example.movie_review.domain.DTO.ReviewCreateRequest;
import com.example.movie_review.domain.review.Review;
import com.example.movie_review.domain.User;
import com.example.movie_review.repository.ReviewRepository;
import com.example.movie_review.repository.UserRepository;
import com.example.movie_review.service.ReviewService;
import com.example.movie_review.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.movie_review.domain.ENUM.UserRole;


@Component
@RequiredArgsConstructor
public class MakeInitData {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final UserService userService;

    @PostConstruct
    public void makeData() {
        makeAdminAndUser();
        makeReview();
    }

    private void makeAdminAndUser() {
        User admin1 = User.builder()
                .loginId("admin1")
                .password("1234")
                .nickname("관리자1")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin1);

        User user1 = User.builder()
                .loginId("user1")
                .password("1234")
                .nickname("User1")
                .role(UserRole.USER)
                .build();
        userRepository.save(user1);

        User user3 = User.builder()
                .loginId("user3")
                .password("1234")
                .nickname("User3")
                .role(UserRole.USER)
                .build();
        userRepository.save(user3);

        User admin2 = User.builder()
                .loginId("admin2")
                .password(encoder.encode("1234"))
                .nickname("관리자")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin2);

        User user2 = User.builder()
                .loginId("user")
                .password(encoder.encode("1234"))
                .nickname("유저1")
                .role(UserRole.USER)
                .build();
        userRepository.save(user2);


    }

    private void makeReview() {
        User user1 = userRepository.findByLoginId("user1").get();
        User user2 = userRepository.findByLoginId("user3").get();

        Review review1 = new Review();
        ReviewCreateRequest reviewCreateRequest1 = new ReviewCreateRequest(4.5, "듄: 파트2", "듄 1보다 더 재밌는 개꿀잼 SF 영화", null);
        review1.updateReview(reviewCreateRequest1, user1);
        reviewService.saveReview(review1);

        ReviewCreateRequest reviewCreateRequest2 = new ReviewCreateRequest(4.0, "파묘", "조대희 선정 한국 오컬트 영화 베스트 10(아직 안봄)", null);
        Review review2 = new Review();
        review2.updateReview(reviewCreateRequest2, user2);
        reviewService.saveReview(review2);

        ReviewCreateRequest reviewCreateRequest3 = new ReviewCreateRequest(5.0, "라라랜드", "뮤지컬 영화 너무 재밌다", null);
        Review review3 = new Review();
        review3.updateReview(reviewCreateRequest3, user2);
        reviewService.saveReview(review3);

        ReviewCreateRequest reviewCreateRequest4 = new ReviewCreateRequest(4.0, "애스터로이드 시티", "색감, 카메라 구도, 배우", null);
        Review review4 = new Review();
        review4.updateReview(reviewCreateRequest4, user1);
        reviewService.saveReview(review4);

        ReviewCreateRequest reviewCreateRequest5 = new ReviewCreateRequest(3.5, "다우트", "의심 ㄴ", null);
        Review review5 = new Review();
        review5.updateReview(reviewCreateRequest5, user1);
        reviewService.saveReview(review5);

        ReviewCreateRequest reviewCreateRequest6 = new ReviewCreateRequest(3.5, "프렌치 디스패치", "신문 재밌는거였네", null);
        Review review6 = new Review();
        review6.updateReview(reviewCreateRequest6, user1);
        reviewService.saveReview(review6);

        ReviewCreateRequest reviewCreateRequest7 = new ReviewCreateRequest(3.0, "그린 나이트", "이거 그린나이트인가요?", null);
        Review review7 = new Review();
        review7.updateReview(reviewCreateRequest7, user2);
        reviewService.saveReview(review7);

        ReviewCreateRequest reviewCreateRequest8 = new ReviewCreateRequest(4.5, "그래비티", "어쩔티비", null);
        Review review8 = new Review();
        review8.updateReview(reviewCreateRequest8, user2);
        reviewService.saveReview(review8);

        ReviewCreateRequest reviewCreateRequest9 = new ReviewCreateRequest(5.0, "다크나이트", "그 나이트 아니였어?", null);
        Review review9 = new Review();
        review9.updateReview(reviewCreateRequest9, user1);
        reviewService.saveReview(review9);

        ReviewCreateRequest reviewCreateRequest10 = new ReviewCreateRequest(5.0, "헤어질결심", "조대희 선정 감정선 폭발하는 영화 탑 쓰리", null);
        Review review10 = new Review();
        review10.updateReview(reviewCreateRequest10, user2);
        reviewService.saveReview(review10);







//        review1.setTitle("듄: 파트2");
//        review1.setContext("듄 1보다 더 재밌는 개꿀잼 SF 영화");
//        review1.setScore(4.5);
//        review1.setUser(user);
//        review1.setHeartCnt(0);

//        Review review2 = new Review();
//        review2.setTitle("파묘");
//        review2.setContext("조대희 선정 한국 오컬트 영화 베스트 10(아직 안봄)");
//        review2.setScore(4.0);
//        review2.setUser(user);
//        review2.setHeartCnt(0);
//        reviewService.saveReview(review2);
//
//        Review review3 = new Review();
//        review3.setTitle("라라랜드");
//        review3.setContext("뮤지컬 영화 너무 재밌다");
//        review3.setScore(5.0);
//        review3.setUser(user);
//        review3.setHeartCnt(0);
//        reviewService.saveReview(review3);
//
//        Review review4 = new Review();
//        review4.setTitle("애스터로이드 시티");
//        review4.setContext("색감, 카메라 구도, 배우");
//        review4.setScore(4.0);
//        review4.setUser(user);
//        review4.setHeartCnt(0);
//        reviewService.saveReview(review4);
//
//        Review review5 = new Review();
//        review5.setTitle("다우트");
//        review5.setContext("의심 ㄴ");
//        review5.setScore(3.5);
//        review5.setUser(user);
//        review5.setHeartCnt(0);
//        reviewService.saveReview(review5);
//
//        Review review6 = new Review();
//        review6.setTitle("프렌치 디스패치");
//        review6.setContext("신문 재밌는거였네");
//        review6.setScore(3.5);
//        review6.setUser(user);
//        review6.setHeartCnt(0);
//        reviewService.saveReview(review6);
//
//        Review review7 = new Review();
//        review7.setTitle("그린 나이트");
//        review7.setContext("이거 그린나이트인가요?");
//        review7.setScore(3.0);
//        review7.setUser(user);
//        review7.setHeartCnt(0);
//        reviewService.saveReview(review7);
//
//        Review review8 = new Review();
//        review8.setTitle("그래비티");
//        review8.setContext("어쩔티비");
//        review8.setScore(4.5);
//        review8.setUser(user);
//        review8.setHeartCnt(0);
//        reviewService.saveReview(review8);
//
//        Review review9 = new Review();
//        review9.setTitle("다크나이트");
//        review9.setContext("그 나이트 아니였어?");
//        review9.setScore(5.0);
//        review9.setUser(user);
//        review9.setHeartCnt(0);
//        reviewService.saveReview(review9);
//
//        Review review10 = new Review();
//        review10.setTitle("헤어질결심");
//        review10.setContext("조대희 선정 감정선 폭발하는 영화 탑 쓰리");
//        review10.setScore(5.0);
//        review10.setUser(user);
//        review10.setHeartCnt(0);
//        reviewService.saveReview(review10);

    }

}