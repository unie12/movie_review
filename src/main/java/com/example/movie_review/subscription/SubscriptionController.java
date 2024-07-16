package com.example.movie_review.subscription;

import com.example.movie_review.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * 현재 사용자가(auth) 해당 사용자를(userEmail) 구독했는지 확인
     */
    @GetMapping("/check/{userEmail}")
    public boolean checkSubscription(@PathVariable String userEmail, Authentication auth) {
        return subscriptionService.isSubscribed(auth.getName(), userEmail);
    }

    @PostMapping("/subscribe/{userEmail}")
    public boolean subscribe(@PathVariable String userEmail, Authentication auth) {
        return subscriptionService.subscribe(auth.getName(), userEmail);
    }

    @PostMapping("/unsubscribe/{userEmail}")
    public boolean unsubscribe(@PathVariable String userEmail, Authentication auth) {
        return subscriptionService.unsubscribe(auth.getName(), userEmail);
    }
}
