package com.example.movie_review.controller;

import com.example.movie_review.domain.DTO.CommentCreateRequest;
import com.example.movie_review.service.CommentService;
import com.example.movie_review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ReviewService reviewService;

    @PostMapping("/{reviewId}")
    public String addComments(@PathVariable Long reviewId, @ModelAttribute CommentCreateRequest req, Authentication auth, Model model) {
        commentService.writeComment(reviewId, auth.getName(), req);

        model.addAttribute("message", "댓글이 추가되었습니다.");
        model.addAttribute("nextUrl", "/reviews/" + reviewId);
        return "printMessage";
    }

    @PostMapping("/{commentId}/edit")
    public String editComment(@PathVariable Long commentId, @ModelAttribute CommentCreateRequest req, Authentication auth, Model model) {
        Long reviewId = commentService.editComment(commentId, req.getBody(), auth.getName());
        model.addAttribute("message", reviewId == null ? "잘못된 요청입니다." : "댓글이 수정되었습니다.");
        model.addAttribute("nextUrl", "/reviews/" + reviewId);
        return "printMessage";
    }

    @GetMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, Authentication auth, Model model) {
        Long reviewId = commentService.deleteComment(commentId, auth.getName());
        model.addAttribute("message", reviewId == null ? "작성자만 삭제 가능합니다." : "댓글이 삭제되었습니다.");
        model.addAttribute("nextUrl", "/reviews/" + reviewId);
        return "printMessage";
    }
}
