package com.example.movie_review.tmdb;

import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
//@RequiredArgsConstructor
public class TmdbService {
    private final WebClient webClient;

    @Value("${tmdb.api.key}")
    private String apikey;

    public TmdbService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public Mono<String> getPopularMovies() {
        return this.webClient.get()
                .uri("/movie/popular?api_key={apiKey}&language=ko-KR", apikey)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> searchMovies(String query) {
        return this.webClient.get()
                .uri("/search/movie?api_key={api_key}&query={query}&language=ko-KR", apikey, query)
                .retrieve()
                .bodyToMono(String.class);
    }


}
