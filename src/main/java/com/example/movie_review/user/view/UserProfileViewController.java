package com.example.movie_review.user.view;

import com.example.movie_review.user.DTO.UserProfileDTO;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserProfileDTOService;
import com.example.movie_review.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/additional-info")
public class UserProfileViewController {

    private final UserProfileDTOService userProfileDTOService;
    private final UserService userService;
    /**
     * 사용자 추가 정보 처리e
     */
    @GetMapping("/{userEmail}")
    public String additionalInfoPage(@PathVariable String userEmail,  Model model) {
        User user = userService.getUserByEmail(userEmail);
        UserProfileDTO userProfileDTO = userProfileDTOService.getUserProfileDTO(userEmail);
        if (user.getNickname().startsWith("temp_")) {
            model.addAttribute("isNewUser", true);
        } else {
            model.addAttribute("isNewUser", false);
        }
        model.addAttribute("userProfile", userProfileDTO);

        return "additional-info";
    }

}
