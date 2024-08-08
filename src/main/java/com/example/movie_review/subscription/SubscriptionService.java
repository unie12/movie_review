package com.example.movie_review.subscription;

import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.repository.UserRepository;
import com.example.movie_review.user.service.UserDTOService;
import com.example.movie_review.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

//    private final SubscriptionService subscriptionService;

    public boolean isSubscribed(String subscriberEmail, String subscribedEmail) {
        User subscriber = getUser(subscriberEmail);
        User subscribed = getUser(subscribedEmail);

        return subscriptionRepository.existsBySubscriberAndSubscribed(subscriber, subscribed);
    }

    @Transactional
    public boolean toggleSubscription(String subscriberEmail, String subscribedEmail) {
        User subscriber= getUser(subscriberEmail);
        User subscribed = getUser(subscribedEmail);

        Subscription savedSubscription = subscriptionRepository.findBySubscriberAndSubscribed(subscriber, subscribed);

        if (savedSubscription == null) {
            Subscription subscription = new Subscription();
            subscription.setSubscriber(subscriber);
            subscription.setSubscribed(subscribed);
            subscription.setSubscriptionDate(LocalDateTime.now());
            subscriptionRepository.save(subscription);
            return true;
        }
        else {
            subscriptionRepository.delete(savedSubscription);
            return false;
        }
    }

    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
