package com.example.movie_review.feedback;

import com.example.movie_review.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public void save(User user, String feedbackText) {
        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setFeedbackText(feedbackText);
        feedbackRepository.save(feedback);
    }
}
