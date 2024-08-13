package com.example.movie_review.user.api;

import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserActivityController {

    private final Map<String, UserActivityService> activityServices;


    /**
     * 해당 사용자의 각 카테고리별 활동 내역 리스트 보여주기
     */
    @GetMapping("/{userEmail}/activities")
    public ResponseEntity<?> getUserActivities(
            @PathVariable String userEmail,
            @RequestParam String category,
            @RequestParam(defaultValue = "createdAt_desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Authentication auth) {

        UserActivityService service = activityServices.get(category);
        if(service == null) {
            return ResponseEntity.badRequest().body("Invalid category");
        }

        UserActivityDTO activities = service.getUserActivity(auth.getName(), userEmail, sort, page, size);

        Map<String, Object> response = activities.toMap();
        response.put("category", category);
        response.put("hasMore", activities.getActivityItems().size() == size);

        return ResponseEntity.ok(response);
    }

    /**
     * 각 카테고리별 sort-option 리스트 보여주기
     */
    @GetMapping("/{userEmail}/sort-options")
    public ResponseEntity<Map<String , List<SortOption>>> getSortOptions(@PathVariable String userEmail) {
        Map<String, List<SortOption>> sortOptions = new HashMap<>();
        for (Map.Entry<String, UserActivityService> entry : activityServices.entrySet()) {
            sortOptions.put(entry.getKey(), entry.getValue().getSortOptions());
        }

        return ResponseEntity.ok(sortOptions);
    }
}
