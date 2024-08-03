package com.example.movie_review.user.DTO;

import java.util.List;
import java.util.Map;

public class UserActivityDTOAdapter implements UserActivityDTO {
    private final UserActivityDTO originalDTO;

    public UserActivityDTOAdapter(UserActivityDTO dto) {
        this.originalDTO = dto;
    }

    @Override
    public UserCommonDTO getUserCommonDTO() {
        return originalDTO.getUserCommonDTO();
    }

    @Override
    public List<?> getActivityItems() {
        return originalDTO.getActivityItems();
    }

    @Override
    public Map<String, Object> toMap() {
        return originalDTO.toMap();
    }

    public Object getOriginalDTO() {
        return originalDTO;
    }


}
