package com.example.movie_review.user;

import com.example.movie_review.genre.GenresRepository;
import com.example.movie_review.user.DTO.UserProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserRepository userRepository;
    private final GenresRepository genresRepository;

}
