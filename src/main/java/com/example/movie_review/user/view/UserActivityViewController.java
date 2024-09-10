package com.example.movie_review.user.view;

import com.example.movie_review.review.service.ReviewService;
import com.example.movie_review.subscription.SubscriptionService;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.DTO.UserDTO;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.CategoryService;
import com.example.movie_review.user.service.UserDTOService;
import com.example.movie_review.user.service.UserActivityService;
import com.example.movie_review.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@Controller
@RequestMapping("/info/{userEmail}")
@RequiredArgsConstructor
public class UserActivityViewController {

    private final UserDTOService userDTOService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;

    private final Map<String, UserActivityService> activityServices;

    /**
     * 사용자 활동 내역 보기
     */
    @GetMapping("")
    public String userInfo(@PathVariable String userEmail, Model model, Authentication auth) throws AccessDeniedException {
        UserDTO userDTO = userDTOService.getuserDTO(userEmail);
        boolean isSubscribed = subscriptionService.isSubscribed(auth.getName(), userEmail);
        User currentUSer = userService.getUserByEmail(auth.getName());

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("isSubscribed", isSubscribed);
        model.addAttribute("currentUser", currentUSer.getNickname());
        return "info";
    }

    /**
     * 사용자 활동 내역 리스트
     */
    @GetMapping("/{category}")
    public String getUserInfo(@PathVariable String userEmail, @PathVariable String category, Model model, Authentication auth) throws AccessDeniedException {

        //        Info permission 부여
//        if(!user.getEmail().equals(auth.getName())) {
//            throw new AccessDeniedException("You don't have permission to view this user");
//        }

        UserActivityService service = activityServices.get(category);
        if (service == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category");
        }
        User user = userService.getUserByEmail(userEmail);
        UserCommonDTO userDTO = userDTOService.getUserCommonDTO(user);

//        UserActivityDTO activityDTO = service.getUserActivity(userEmail, "default", 0, 20);
//        model.addAttribute("activities", ((UserActivityDTOAdapter) activityDTO).getOriginalDTO());
        model.addAttribute("category", category);
        model.addAttribute("user", userDTO);
        model.addAttribute("pageTitle", categoryService.getCategoryTitle(category));
        model.addAttribute("emptyMessage", categoryService.getEmptyMessage(category));
        model.addAttribute("defaultSort", categoryService.getDefaultSort(category));

        return "user-activity/user-activity";
//        return "/user-activity/user-" + category;
    }
}
