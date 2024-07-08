package com.example.movie_review.kobis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.channels.MembershipKey;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@Service
public class KobisService {
    private final WebClient webClient;

    @Value("${kobis.api.key}")
    private String apikey;

    public KobisService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://kobis.or.kr/kobisopenapi/webservice/rest").build();
    }
    public Mono<String> getDailyBoxOfficeMovies() {
        LocalDate today = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = today.format(formatter);

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/boxoffice/searchDailyBoxOfficeList.json")
                        .queryParam("key", apikey)
                        .queryParam("targetDt", formattedDate)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getWeeklyBoxOfficeMovies() {
        LocalDate today = LocalDate.now();
        LocalDate targetDate;

        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {
            // 월요일이면 어제(일요일)의 날짜를 사용
            targetDate = today.minusDays(1);
        } else {
            // 월요일이 아니면 지난 주 일요일의 날짜를 사용
            targetDate = today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = targetDate.format(formatter);

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/boxoffice/searchWeeklyBoxOfficeList.json")
                        .queryParam("key", apikey)
                        .queryParam("targetDt", formattedDate)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> searchMovie(String title) {
        return webClient.get()
                .uri("/movie/searchMovieList.json?key={apiKey}&movieNm={title}", apikey, title)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getMovieInfo(String movieCd) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/searchMovieInfo.json")
                        .queryParam("key", apikey)
                        .queryParam("movieCd", movieCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
