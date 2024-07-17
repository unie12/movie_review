package com.example.movie_review.subscription;

import com.example.movie_review.user.DTO.UserDTO;
import com.example.movie_review.user.DTO.UserDTOService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription/{userEmail}")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserDTOService userDTOService;

    /**
     * 현재 사용자가(auth) 해당 사용자를(userEmail) 구독했는지 확인
     */
    @GetMapping("")
    public ResponseEntity<SubscriptionResponse> checkSubscription(@PathVariable String userEmail, Authentication auth) {
        boolean isSubscribed = subscriptionService.isSubscribed(auth.getName(), userEmail);
        return ResponseEntity.ok(new SubscriptionResponse(isSubscribed));
    }

    /**
     * 구독 toggle
     */
    @PutMapping("")
    public ResponseEntity<SubscriptionResponse> toggleSubscription(@PathVariable String userEmail, Authentication auth) {
        boolean isSubscribed = subscriptionService.toggleSubscription(auth.getName(), userEmail);
        return ResponseEntity.ok(new SubscriptionResponse(isSubscribed));
    }

    /**
     * 나를 구독하고 있는 구독자 목록 조회
     */
    @GetMapping("/subscribers")
    public ResponseEntity<List<UserDTO>> getSubscribers(@PathVariable String userEmail) {
        List<UserDTO> subscribers = userDTOService.getSubscribers(userEmail);
        return ResponseEntity.ok(subscribers);
    }
    /**
     * 내가 구독하고 있는 구독자 목록 조회
     */
    @GetMapping("/subscriptions")
    public ResponseEntity<List<UserDTO>> getSubscriptions(@PathVariable String userEmail) {
        List<UserDTO> subscriptions = userDTOService.getSubscriptions(userEmail);
        return ResponseEntity.ok(subscriptions);
    }

}

@Data
@AllArgsConstructor
class SubscriptionResponse {
    private boolean subscribed;
}

