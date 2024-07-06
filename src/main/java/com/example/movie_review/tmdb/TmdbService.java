package com.example.movie_review.tmdb;

import com.example.movie_review.kobis.KobisService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.channels.MembershipKey;

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

    public Mono<String> getTrendingMovies() {
        return this.webClient.get()
                .uri("/trending/movie/day?api_key={apiKey}&language=ko-KR", apikey)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> searchMovies(String query) {
        return this.webClient.get()
                .uri("/search/movie?api_key={api_key}&query={query}&language=ko-KR", apikey, query)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<JsonNode> searchJsonMovie(String query) {
        return this.webClient.get()
                .uri("/search/movie?api_key={api_key}&query={query}&language=ko-KR", apikey, query)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::converToJsonNode);
    }

    private JsonNode converToJsonNode(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON string to JsonNode", e);
        }
    }


    public Mono<String> getMovieDetails(Long movieId) {
        return webClient.get()
                .uri("/movie/" + movieId + "?api_key={api_key}&language=ko-KR&append_to_response=credits", apikey)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getActorDetails(Long actorId) {
        return webClient.get()
                .uri("/person/" + actorId + "/combined_credits?api_key={api_key}&language=ko-KR", apikey)
                .retrieve()
                .bodyToMono(String.class);
    }
}

