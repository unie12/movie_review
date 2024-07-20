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

@Service("subscriber")
@RequiredArgsConstructor
public class SubscriberService implements UserActivityService {
    private final UserDTOService userDTOService;

    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        SubscriptionDTO dto = userDTOService.getUserSubscribersDTO(userEmail);
        List<SubscriptionInfo> sortedSubscriptions = sortSubscriptions(dto.getSubscriptionDTOs(), sort);
        dto.setSubscriptionDTOs(sortedSubscriptions);

        return new UserActivityDTOAdapter(dto);
    }
    private List<SubscriptionInfo> sortSubscriptions(List<SubscriptionInfo> subscriptions, String sort) {
        switch (sort) {
            case "subscriber_date_desc":
                return subscriptions.stream()
                        .sorted((a, b) -> b.getSubscriptionDate().compareTo(a.getSubscriptionDate()))
                        .collect(Collectors.toList());
            case "subscriber_date_asc":
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
                new SortOption("subscriber_date_desc", "구독자 최근"),
                new SortOption("subscriber_date_asc", "구독자 과거")
        );    }
}
