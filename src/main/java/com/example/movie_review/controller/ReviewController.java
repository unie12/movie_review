//package com.example.movie_review.controller;
//
//import com.example.movie_review.auth.JwtTokenUtil;
//import com.example.movie_review.domain.DTO.CommentCreateRequest;
//import com.example.movie_review.domain.DTO.ReviewCreateRequest;
//import com.example.movie_review.domain.DTO.ReviewDto;
//import com.example.movie_review.domain.ENUM.SortType;
//import com.example.movie_review.domain.ReviewSpecifications;
//import com.example.movie_review.user.User;
//import com.example.movie_review.domain.review.Review;
//import com.example.movie_review.domain.review.ReviewImage;
//import com.example.movie_review.service.*;
//import com.example.movie_review.user.UserService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//@RequiredArgsConstructor
//public class ReviewController {
//
//    private final ReviewService reviewService;
//    private final UserService userService;
//    private final HeartService heartService;
//    private final CommentService commentService;
//    private final ReviewImageService reviewImageService;
//
//
//    /**
//     * 리뷰 목록들
//     * sortType과 searchType, keyword에 따라 검색 기능
//     *
//     */
//    @GetMapping("/reviews")
//    public String review(@RequestParam(required = false, defaultValue = "최근 등록순") String sortType,
//                         @RequestParam(required = false, defaultValue = "10") int pageSize,
//                         @RequestParam(required = false, defaultValue = "title") String searchType,
//                         @RequestParam(required = false, defaultValue = "") String keyword,
//                         Pageable pageable, Model model) {
//        Sort sort = SortType.getSort(sortType);
//        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageSize, sort);
//        System.out.println("searchType = " + searchType + " keyword = " + keyword);
//
//        Specification<Review> spec = ReviewSpecifications.hasKeyword(keyword, searchType);
//        Page<Review> reviews = reviewService.findReviews(spec, sortedPageable);
//
//        model.addAttribute("reviews", reviews);
//        model.addAttribute("sortType", sortType);
//        model.addAttribute("pageSize", pageSize);
//        model.addAttribute("searchType", searchType);
//
//        return "reviews";
//    }
//
//    /**
//     * 새로운 리뷰 작성
//     */
//    @GetMapping("/createReview")
//    public String createReviewForm(Model model) {
//        model.addAttribute("reviewCreateRequest", new ReviewCreateRequest(null, null, null, null));
//        return "createReview";
//    }
//
//
//    @PostMapping("/createReview")
//    public String createReview(@ModelAttribute ReviewCreateRequest reviewCreateRequest, HttpServletRequest request) throws IOException {
//        User user = new User();
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null) {
//            for(Cookie cookie : cookies) {
//                if(cookie.getName().equals("jwtToken")) {
//                    String jwtToken = cookie.getValue();
//                    String secretKey = "my-secret-key-123123";
//                    String loginId = JwtTokenUtil.getLoginId(jwtToken, secretKey);
//                    user = userService.getLoginUserByLoginId(loginId);
//                    break;
//                }
//            }
//        }
//        Review review = new Review();
//        review.updateReview(reviewCreateRequest, user);
////        review.setUser(user);
//
//        ReviewImage reviewImage = reviewImageService.saveImage(reviewCreateRequest.getReviewImage(), review);
//        if(reviewImage != null) {
//            review.setReviewImage(reviewImage);
//
//            String savedFilename = reviewImage.getSavedFilename();
//            System.out.println("savedFilename = " + savedFilename);
//
//            String fullPath = reviewImageService.getFullPath(savedFilename);
//            System.out.println("fullPath = " + fullPath);
//            review.setFilePath(savedFilename);
//        }
//
//
//        reviewService.saveReview(review);
//        return "redirect:/reviews";
//    }
//
//    /**
//     * 리뷰 상세 정보 보기
//     */
//    @GetMapping("/reviews/{reviewId}")
//    public String viewReview(@PathVariable Long reviewId, Model model, Authentication auth) {
//        if(auth != null) {
//            model.addAttribute("loginUserLoginId", auth.getName());
//            model.addAttribute("heartCheck", heartService.checkHeart(auth.getName(), reviewId));
//        }
//
//        Review review = reviewService.findOne(reviewId);
//        ReviewDto reviewDto = reviewService.getReview(reviewId);
//
//        if(review == null) {
//            return "redirect:/reviews";
//        }
//
//        review.setViewCount(review.getViewCount() + 1);
////        model.addAttribute("review", review);
//        model.addAttribute("review", reviewDto);
//        model.addAttribute("uploadDate", review.getUploadDate());
//        model.addAttribute("commentCreateRequest", new CommentCreateRequest());
//        model.addAttribute("commentList", commentService.findAll(reviewId));
//        reviewService.saveReview(review);
//        return "viewReview";
//    }
//
//    @GetMapping("/reviews/{reviewId}/addHeart")
//    @ResponseBody
//    public Map<String, Integer> addHeart(@PathVariable Long reviewId, Authentication auth) {
//        heartService.addHeart(auth.getName(), reviewId);
//        Review review = reviewService.findOne(reviewId);
//        Map<String, Integer> response = new HashMap<>();
//        response.put("heartCnt", review.getHeartCnt());
//        return response;
//    }
//
//    @GetMapping("/reviews/{reviewId}/deleteHeart")
//    @ResponseBody
//    public Map<String, Integer> deleteHeart(@PathVariable Long reviewId, Authentication auth) {
//        heartService.deleteHeart(auth.getName(), reviewId);
//        Review review = reviewService.findOne(reviewId);
//        Map<String, Integer> response = new HashMap<>();
//        response.put("heartCnt", review.getHeartCnt());
//        return response;
//    }
//
//    /**
//     * 리뷰 수정
//     */
//    @PostMapping("/reviews/{reviewId}/edit")
//    public String reviewEdit(@PathVariable Long reviewId, @ModelAttribute ReviewDto reviewDto, Model model) throws IOException {
//        Long editReviewId = reviewService.editReview(reviewDto, reviewId);
//        if(editReviewId == null) {
//            model.addAttribute("message", "해당 게시글이 존재하지 않습니다.");
//            model.addAttribute("nextUrl", "/reviews");
//        }
//        else {
//            model.addAttribute("message", editReviewId + "번 글이 수정되었습니다.");
//            model.addAttribute("nextUrl", "/reviews/" + reviewId);
//        }
////        model.addAttribute("reviewDto", reviewDto);
//        return "printMessage";
//    }
//
////    @PostMapping("/reviews/{reviewId}/edit")
////    public String editEnd(@PathVariable Long reviewId, Model model) {
////        return "/reviews/{reviewId}";
////    }
//
//    /**
//     * 페이징
//     */
//
//    /**
//     * 검색
//     */
//
//    /**
//     * 삭제
//     */
//    @GetMapping("/reviews/{reviewId}/delete")
//    public String reviewDelete(@PathVariable Long reviewId, Model model) throws IOException {
//        Long deleteReviewId = reviewService.deleteReview(reviewId);
//        model.addAttribute("message", deleteReviewId == null ? "해당 게시글이 존재하지 않습니다." : deleteReviewId + "번 글이 삭제되었습니다.");
//        model.addAttribute("nextUrl", "/reviews");
//
//        return "printMessage";
//    }
//
//}
