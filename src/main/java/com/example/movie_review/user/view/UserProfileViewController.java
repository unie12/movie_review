package com.example.movie_review.user.view;

import com.example.movie_review.user.DTO.UserProfileDTO;
import com.example.movie_review.user.DTO.UserProfileDTOService;
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
    /**
     * 사용자 추가 정보 처리e
     */
    @GetMapping("/{userEmail}")
    public String additionalInfoPage(@PathVariable String userEmail,  Model model) {
        UserProfileDTO userProfileDTO = userProfileDTOService.getUserProfileDTO(userEmail);
        model.addAttribute("userProfile", userProfileDTO);

        return "additional-info";
    }

}
