package com.example.movie_review.subscription;

import com.example.movie_review.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsBySubscriberAndSubscribed(User subscriber, User subscribed);

    Subscription findBySubscriberAndSubscribed(User subscriber, User subscribed);

}
