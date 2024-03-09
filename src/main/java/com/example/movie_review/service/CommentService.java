package com.example.movie_review.service;

import com.example.movie_review.domain.review.Comment;
import com.example.movie_review.domain.DTO.CommentCreateRequest;
import com.example.movie_review.domain.review.Review;
import com.example.movie_review.domain.User;
import com.example.movie_review.domain.ENUM.UserRole;
import com.example.movie_review.repository.CommentRepository;
import com.example.movie_review.repository.ReviewRepository;
import com.example.movie_review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 작성
     */
    public void writeComment(Long reviewId, String loginId, CommentCreateRequest req) {
        Review review = reviewRepository.findById(reviewId).get();
        User user = userRepository.findByLoginId(loginId).get();
        review.commentChange(review.getCommentCnt() + 1);
        commentRepository.save(req.toEntity(review, user));
    }

    /**
     * 댓글 모두 찾기
     */
    public List<Comment> findAll(Long reviewId) {
        return commentRepository.findAllByReviewId(reviewId);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Long editComment(Long commentId, String newBody, String loginId) {
        Optional<Comment> optionComment = commentRepository.findById(commentId);
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);

        if(optionComment.isEmpty() || optionalUser.isEmpty() || !optionComment.get().getUser().equals(optionalUser.get())) {
            return null;
        }

        Comment comment = optionComment.get();
        comment.update(newBody);

        return comment.getReview().getId();
    }

    /**
     * 댓글 삭제
     */
    public Long deleteComment(Long commentId, String loginId) {
        Optional<Comment> optionComment = commentRepository.findById(commentId);
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionComment.isEmpty() || optionalUser.isEmpty() || (!optionComment.get().getUser().equals(optionalUser.get()) && !optionalUser.get().getRole().equals(UserRole.ADMIN))) {
            return null;
        }

        Review review = optionComment.get().getReview();
        review.commentChange(review.getCommentCnt()-1);

        commentRepository.delete(optionComment.get());
        return review.getId();
    }
}
