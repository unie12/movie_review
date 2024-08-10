package com.example.movie_review.review.event;

import com.example.movie_review.review.Review;
import org.springframework.context.ApplicationEvent;

public class ReviewEvent extends ApplicationEvent {
    private final Review review;
    private final ReviewEventType eventType;

    public enum ReviewEventType {
        CREATED, UPDATED, DELETED, HEART
    }

    public ReviewEvent(Object source, Review review,ReviewEventType eventType) {
        super(source);
        this.review = review;
        this.eventType = eventType;
    }

    public Review getReview() {
        return review;
    }

    public ReviewEventType getEventType() {
        return eventType;
    }
}
