package com.example.movie_review.user.service;

import com.example.movie_review.user.DTO.SubscriptionDTO;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.DTO.UserActivityDTOAdapter;
import com.example.movie_review.user.DTO.UserDTOService;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("subscription")
@RequiredArgsConstructor
public class SubscriptionService implements UserActivityService {
    private final UserDTOService userDTOService;

    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        SubscriptionDTO dto = userDTOService.getUserSubscriptionsDTO(userEmail);
        return new UserActivityDTOAdapter(dto);
    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("favorite_date_desc", "구독 최근"),
                new SortOption("favorite_date_asc", "구독 과거")
        );    }
}
