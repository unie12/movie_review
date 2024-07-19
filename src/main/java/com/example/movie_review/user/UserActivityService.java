package com.example.movie_review.user;

import com.example.movie_review.user.DTO.UserActivityDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserActivityService {
    UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size);
    List<SortOption> getSortOptions();
}
