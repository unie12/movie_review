package com.example.movie_review.review.view;

import com.example.movie_review.review.DTO.ReviewMovieDTO;
import com.example.movie_review.review.service.ReviewMovieDTOService;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReviewViewController {

    private final ReviewMovieDTOService reviewMovieDTOService;
    private final UserService userService;
    /**
     * @return 해당 tid의 영화에 관한 리뷰들 보여주기
     */
    @GetMapping("/contents/{movieTId}/reviews")
    public String movieReviews(@PathVariable Long movieTId, Model model) {
        model.addAttribute("movieTId", movieTId);
        return "movieReviews";
    }

    /**
     * 전체 리뷰들 보기
     */
    @GetMapping("/reviews")
    public String getAllReviews(@RequestParam(defaultValue = "popular") String filter,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) Long highlight,
                                Authentication auth,
                                Model model) {
//        reviewService.checkAndUpdateCache();
        Page<ReviewMovieDTO> reviewPage;
        User userByEmail = userService.getUserByEmail(auth.getName());

        if("recently".equals(filter)) {
            reviewPage = reviewMovieDTOService.getRecentReviews(PageRequest.of(page, size), userByEmail);
        } else {
            reviewPage = reviewMovieDTOService.getPopularReviews(PageRequest.of(page, size), userByEmail);
        }

        reviewPage.getContent().forEach(reviews -> {
            String reviewTextWithBreaks = reviews.getReviewDTO().getReview().getText().replace("\n", "<br>");
            reviews.getReviewDTO().getReview().setText(reviewTextWithBreaks);
        });


        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentFilter", filter);
        model.addAttribute("currentPage", reviewPage.getNumber());
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("highlightReviewId", highlight);

        return "reviews";
    }
    
    
}
