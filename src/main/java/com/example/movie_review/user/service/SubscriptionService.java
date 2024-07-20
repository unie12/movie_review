package com.example.movie_review.user.service;

import com.example.movie_review.user.DTO.*;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("subscription")
@RequiredArgsConstructor
public class SubscriptionService implements UserActivityService {
    private final UserDTOService userDTOService;

    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        SubscriptionDTO dto = userDTOService.getUserSubscriptionsDTO(userEmail);
        List<SubscriptionInfo> sortedSubscriptions = sortSubscriptions(dto.getSubscriptionDTOs(), sort);
        dto.setSubscriptionDTOs(sortedSubscriptions);

        return new UserActivityDTOAdapter(dto);
    }

    private List<SubscriptionInfo> sortSubscriptions(List<SubscriptionInfo> subscriptions, String sort) {
        switch (sort) {
            case "subscription_date_desc":
                return subscriptions.stream()
                        .sorted((a, b) -> b.getSubscriptionDate().compareTo(a.getSubscriptionDate()))
                        .collect(Collectors.toList());
            case "subscription_date_asc":
                return subscriptions.stream()
                        .sorted(Comparator.comparing(SubscriptionInfo::getSubscriptionDate))
                        .collect(Collectors.toList());
            default:
                return subscriptions;
        }
    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("subscription_date_desc", "구독 최근"),
                new SortOption("subscription_date_asc", "구독 과거")
        );    }
}
