package com.example.movie_review.user;

import com.example.movie_review.oauth.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @GetMapping("/additional-info")
    public String additionalInfoPage(Model model) {
//        User user = (User) httpSession.getAttribute("user");
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        model.addAttribute("user", sessionUser);
        return "additional-info";
    }

    @PostMapping("/additional-info")
    public String saveAdditionalInfo(@RequestParam String gender,
                                     @RequestParam String nickname,
                                     @RequestParam Long age,
                                     @RequestParam String mbti){
//                                     @RequestParam List<String> preferMovies) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        user.setGender(gender);
        user.setNickname(nickname);
        user.setAge(age);
        user.setMbti(mbti);
//        user.setPreferGenres(preferGenres);
//        user.setPreferMovies(preferMovies);

        userRepository.save(user);
        System.out.println("usercontrollerr = " + user.getNickname());

        // 세션 정보 업데이트
        httpSession.setAttribute("user", new SessionUser(user));

        return "redirect:/jwt-login";
    }
}
