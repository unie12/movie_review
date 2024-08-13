package com.example.movie_review.user.service;

import com.example.movie_review.user.DTO.*;
import com.example.movie_review.user.SortOption;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("subscriber")
public class SubscriberService extends AbstractUserActivityService {
    public SubscriberService(UserDTOService userDTOService) {
        super(userDTOService);
    }

    @Override
    public UserActivityDTO getUserActivity(String authEmail, String userEmail, String sort, int page, int size) {
        SubscriptionDTO dto = userDTOService.getUserSubscribersDTO(userEmail);

        int start = page * size;
        int end = Math.min(start + size, dto.getSubscriptionDTOs().size());

        List<SubscriptionInfo> sortedSubscriptions = sortSubscriptions(dto.getSubscriptionDTOs().subList(start, end), sort);
        dto.setSubscriptionDTOs(sortedSubscriptions);

        return new UserActivityDTOAdapter(dto);
    }
    private List<SubscriptionInfo> sortSubscriptions(List<SubscriptionInfo> subscriptions, String sort) {
        switch (sort) {
            default:
            case "subscriber_date_desc":
                return subscriptions.stream()
                        .sorted((a, b) -> b.getSubscriptionDate().compareTo(a.getSubscriptionDate()))
                        .collect(Collectors.toList());
            case "subscriber_date_asc":
                return subscriptions.stream()
                        .sorted(Comparator.comparing(SubscriptionInfo::getSubscriptionDate))
                        .collect(Collectors.toList());
            case "subscriber_count_desc":
                return subscriptions.stream()
                        .sorted(Comparator.comparingInt(SubscriptionInfo::getSubscriptionCnt).reversed())
                        .collect(Collectors.toList());
            case "subscriber_count_asc":
                return subscriptions.stream()
                        .sorted(Comparator.comparingInt(SubscriptionInfo::getSubscriptionCnt))
                        .collect(Collectors.toList());
        }
    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("subscriber_date_desc", "구독자 최근"),
                new SortOption("subscriber_date_asc", "구독자 과거"),
                new SortOption("subscriber_count_desc", "구독자 많은순"),
                new SortOption("subscriber_count_asc", "구독자 적은순")
        );
    }

}
