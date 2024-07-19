package com.example.movie_review.user.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SubscriptionDTO {
    private UserCommonDTO userCommonDTO;
    private List<UserCommonDTO> subscriptionDTOs;
}
