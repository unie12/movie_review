package com.example.movie_review.user.DTO;

import java.util.List;
import java.util.Map;

public interface UserActivityDTO {
    UserCommonDTO getUserCommonDTO();
    List<?> getActivityItems();
    Map<String, Object> toMap();
}
