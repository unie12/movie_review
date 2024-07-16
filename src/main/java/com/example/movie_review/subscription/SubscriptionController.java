package com.example.movie_review.subscription;

import com.example.movie_review.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/{userEmail}")
    public ResponseEntity<SubscriptionResponse> checkSubscription(@PathVariable String userEmail, Authentication auth) {
        boolean isSubscribed = subscriptionService.isSubscribed(auth.getName(), userEmail);
        return ResponseEntity.ok(new SubscriptionResponse(isSubscribed));
    }

    @PutMapping("/{userEmail}")
    public ResponseEntity<SubscriptionResponse> toggleSubscription(@PathVariable String userEmail, Authentication auth) {
        boolean isSubscribed = subscriptionService.toggleSubscription(auth.getName(), userEmail);
        return ResponseEntity.ok(new SubscriptionResponse(isSubscribed));
    }

}

@Data
@AllArgsConstructor
class SubscriptionResponse {
    private boolean subscribed;
}