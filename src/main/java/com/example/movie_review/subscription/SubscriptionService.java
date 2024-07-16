package com.example.movie_review.subscription;

import com.example.movie_review.user.User;
import com.example.movie_review.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    public boolean isSubscribed(String subscriberEmail, String subscribedEmail) {
        User subscriber= userRepository.findUserByEmail(subscriberEmail);
        User subscribed = userRepository.findUserByEmail(subscribedEmail);

        return subscriptionRepository.existsBySubscriberAndSubscribed(subscriber, subscribed);
    }

    @Transactional
    public boolean subscribe(String subscriberEmail, String subscribedEmail) {
        User subscriber= userRepository.findUserByEmail(subscriberEmail);
        User subscribed = userRepository.findUserByEmail(subscribedEmail);

        if (!isSubscribed(subscriberEmail, subscribedEmail)) {
            Subscription subscription = new Subscription();
            subscription.setSubscriber(subscriber);
            subscription.setSubscribed(subscribed);
            subscription.setSubscriptionDate(LocalDateTime.now());
            subscriptionRepository.save(subscription);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean unsubscribe(String subscriberEmail, String subscribedEmail) {
        User subscriber= userRepository.findUserByEmail(subscriberEmail);
        User subscribed = userRepository.findUserByEmail(subscribedEmail);

        Subscription subscription = subscriptionRepository.findBySubscriberAndSubscribed(subscriber, subscribed);

        if (subscription != null) {
            subscriptionRepository.delete(subscription);
            return true;
        }
        return false;
    }
}
