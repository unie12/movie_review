package com.example.movie_review.user.api;

import com.example.movie_review.movieDetail.MovieDetailDTOService;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.UserActivityService;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserActivityController {

    private final Map<String, UserActivityService> activityServices;


    @GetMapping("/{userEmail}/activities")
    public ResponseEntity<?> getUserActivities(
            @PathVariable String userEmail,
            @RequestParam String category,
            @RequestParam(defaultValue = "createdAt_desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        UserActivityService service = activityServices.get(category);
        if(service == null) {
            return ResponseEntity.badRequest().body("Invalid category");
        }

        UserActivityDTO activities = service.getUserActivity(userEmail, sort, page, size);

        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{userEmail}/sort-options")
    public ResponseEntity<Map<String , List<SortOption>>> getSortOptions(@PathVariable String userEmail) {
        Map<String, List<SortOption>> sortOptions = new HashMap<>();
        for (Map.Entry<String, UserActivityService> entry : activityServices.entrySet()) {
            sortOptions.put(entry.getKey(), entry.getValue().getSortOptions());
        }

        return ResponseEntity.ok(sortOptions);
    }
}
