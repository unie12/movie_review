package com.example.movie_review.user.service;

import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.SortOption;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserActivityService {
    UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size);
    List<SortOption> getSortOptions();

}
