package com.example.movie_review.tmdb;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class MovieSearchService {
    WebClient webClient = WebClient.builder()
            .codecs(configurer -> configurer
                    .defaultCodecs()
                    .maxInMemorySize(2 * 1024 * 1024))
            .baseUrl("https://api.themoviedb.org/3")
            .build();

    @Value("${tmdb.api.key}")
    private String apikey;


}
