package com.example.movie_review.user.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class SubscriptionDTO implements UserActivityDTO{
    private UserCommonDTO userCommonDTO;
    private List<SubscriptionInfo> subscriptionDTOs;

    @Override
    public List<?> getActivityItems() {
        return subscriptionDTOs;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userCommonDTO", userCommonDTO);
        map.put("subscriptionDTOs", subscriptionDTOs);
        return map;
    }
}
