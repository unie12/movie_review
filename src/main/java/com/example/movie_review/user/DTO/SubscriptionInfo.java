package com.example.movie_review.user.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionInfo {
    private UserCommonDTO userCommonDTO;
    private LocalDateTime subscriptionDate;
    private int subscriptionCnt;
}
