package com.example.movie_review.controller;

import com.example.movie_review.auth.JwtTokenUtil;
import com.example.movie_review.domain.DTO.CommentCreateRequest;
import com.example.movie_review.domain.DTO.ReviewCreateRequest;
import com.example.movie_review.domain.ENUM.SortType;
import com.example.movie_review.domain.User;
import com.example.movie_review.domain.review.Review;
import com.example.movie_review.domain.review.ReviewImage;
import com.example.movie_review.repository.UserRepository;
import com.example.movie_review.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final HeartService heartService;
    private final CommentService commentService;
    private final ReviewImageService reviewImageService;

    /**
     * 리뷰 목록들
     */
    @GetMapping("/reviews")
    public String review(@RequestParam(required = false, defaultValue = "최근 등록순") String sortType,
                         @RequestParam(required = false, defaultValue = "10") int pageSize,
                         Pageable pageable, Model model) {
        Sort sort = SortType.getSort(sortType);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageSize, sort);
        Page<Review> reviews = reviewService.findReviews(sortedPageable);
//        List<Review> reviews = reviewService.findReviews();
        model.addAttribute("reviews", reviews);
        model.addAttribute("sortType", sortType);
        model.addAttribute("pageSize", pageSize);

        return "reviews";
    }

//    @PostMapping("/reviews")
//    public String reviewWrite(@ModelAttribute ReviewCreateRequest req, Authentication auth, Model model) throws IOException {
//        reviewService.writeReview(req, auth.getName(), auth)
//    }

    /**
     * 새로운 리뷰 작성
     */
    @GetMapping("/createReview")
    public String createReviewForm(Model model) {
        model.addAttribute("reviewCreateRequest", new ReviewCreateRequest(null, null, null, null));
        return "createReview";
    }


    @PostMapping("/createReview")
    public String createReview(@ModelAttribute ReviewCreateRequest reviewCreateRequest, HttpServletRequest request) throws IOException {
        User user = new User();
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("jwtToken")) {
                    String jwtToken = cookie.getValue();
                    String secretKey = "my-secret-key-123123";
                    String loginId = JwtTokenUtil.getLoginId(jwtToken, secretKey);
                    user = userService.getLoginUserByLoginId(loginId);
                    break;
                }
            }
        }
        Review review = new Review();
        review.updateReview(reviewCreateRequest, user);
        review.setUser(user);

        ReviewImage reviewImage = reviewImageService.saveImage(reviewCreateRequest.getReviewImage(), review);
        review.setReviewImage(reviewImage);
        String savedFilename = reviewImage.getSavedFilename();
        System.out.println("savedFilename = " + savedFilename);
        String fullPath = reviewImageService.getFullPath(savedFilename);
        System.out.println("fullPath = " + fullPath);
        review.setFilePath(savedFilename);

        reviewService.saveReview(review);
        return "redirect:/reviews";
    }

    /**
     * 리뷰 상세 정보 보기
     */
    @GetMapping("/reviews/{reviewId}")
    public String viewReview(@PathVariable Long reviewId, Model model, Authentication auth) {
        if(auth != null) {
            model.addAttribute("loginUserLoginId", auth.getName());
            model.addAttribute("heartCheck", heartService.checkHeart(auth.getName(), reviewId));
        }

        Review review = reviewService.findOne(reviewId);

        if(review == null) {
            return "redirect:/reviews";
        }

//        System.out.println("reviewview = " + review.getViewCount());
        review.setViewCount(review.getViewCount() + 1);
//        System.out.println("reviewview = " + review.getViewCount());
        model.addAttribute("review", review);
        model.addAttribute("commentCreateRequest", new CommentCreateRequest());
        model.addAttribute("commentList", commentService.findAll(reviewId));
        reviewService.saveReview(review);
        return "viewReview";
    }

    @GetMapping("/reviews/{reviewId}/addHeart")
    @ResponseBody
    public Map<String, Integer> addHeart(@PathVariable Long reviewId, Authentication auth) {
        heartService.addHeart(auth.getName(), reviewId);
        Review review = reviewService.findOne(reviewId);
        Map<String, Integer> response = new HashMap<>();
        response.put("heartCnt", review.getHeartCnt());
        return response;
    }

    @GetMapping("/reviews/{reviewId}/deleteHeart")
    @ResponseBody
    public Map<String, Integer> deleteHeart(@PathVariable Long reviewId, Authentication auth) {
        heartService.deleteHeart(auth.getName(), reviewId);
        Review review = reviewService.findOne(reviewId);
        Map<String, Integer> response = new HashMap<>();
        response.put("heartCnt", review.getHeartCnt());
        return response;
    }

    /**
     * 리뷰 수정
     */

    /**
     * 페이징
     */

    /**
     * 검색
     */

    /**
     * 삭제
     */
}
