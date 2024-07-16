package com.example.movie_review.subscription;

import com.example.movie_review.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    private final UserService userService;
    private final UserDTOService userDTOService;
//    private final SubscriptionService subscriptionService;

    public boolean isSubscribed(String subscriberEmail, String subscribedEmail) {
        User subscriber = userService.getUserByEmail(subscriberEmail);
        User subscribed = userService.getUserByEmail(subscribedEmail);

        return subscriptionRepository.existsBySubscriberAndSubscribed(subscriber, subscribed);
    }

    @Transactional
    public boolean toggleSubscription(String subscriberEmail, String subscribedEmail) {
        User subscriber= userService.getUserByEmail(subscriberEmail);
        User subscribed = userService.getUserByEmail(subscribedEmail);

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

}
