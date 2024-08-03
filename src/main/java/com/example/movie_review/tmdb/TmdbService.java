package com.example.movie_review.tmdb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class TmdbService {
    private final WebClient webClient;

    @Value("${tmdb.api.key}")
    private String apikey;

    /**
     * tmdb api base url
     */
    public TmdbService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    /**
     * Popular movie list 가져오기
     */
    public Mono<String> getPopularMovies() {
        return this.webClient.get()
                .uri("/movie/popular?api_key={apiKey}&language=ko-KR", apikey)
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * Trending movie list 가져오기
     */
    public Mono<String> getTrendingMovies() {
        return this.webClient.get()
                .uri("/trending/movie/day?api_key={apiKey}&language=ko-KR", apikey)
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * 제목으로 영화 찾기
     * @return : Mono<String>
     */
    public Mono<String> searchMovies(String query) {
        return this.webClient.get()
                .uri("/search/movie?api_key={api_key}&query={query}&language=ko-KR", apikey, query)
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * 제목 - 개봉일로 영화 찾기
     * -> kobis - tmdb 제목 자체도 다를 수 있기 때문에 여러 조건으로 검색 (Full, Partial)
     * tmdb - kobis 영화 동일한 지 확인용
     * @return : Mono<JsonNode>
     */
    public Mono<JsonNode> searchJsonMovieWithSim(String query, String openDt) {
        return searchWithFullTitle(query, openDt)
                .flatMap(result -> result.path("results").size() > 0 ? Mono.just(result) : searchWithPartialTitles(query, openDt))
                .map(jsonNode -> findMostSimilarMovie(jsonNode, query, openDt))
                .onErrorResume(e -> {
                    System.err.println("Error searching for movie: " + query + ". Error: " + e.getMessage());
                    return Mono.just(JsonNodeFactory.instance.nullNode());
                });
    }

    private Mono<JsonNode> searchWithFullTitle(String query, String openDt) {
        return performSearch(query, openDt);
    }

    private Mono<JsonNode> searchWithPartialTitles(String query, String openDt) {
        List<String> partialQueries = generatePartialQueries(query);
        return Flux.fromIterable(partialQueries)
                .flatMap(partialQuery -> performSearch(partialQuery, openDt))
                .filter(result -> result.path("results").size() > 0)
                .next()
                .defaultIfEmpty(JsonNodeFactory.instance.objectNode());
    }

    private List<String> generatePartialQueries(String query) {
        List<String> partialQueries = new ArrayList<>();
        String[] words = query.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word).append(" ");
            partialQueries.add(sb.toString().trim());
        }
        // 특수 문자 제거한 쿼리 추가
        partialQueries.add(query.replaceAll("[:\\-]", "").trim());
        return partialQueries;
    }

    private Mono<JsonNode> performSearch(String query, String openDt) {
        String year = (openDt != null && openDt.length() >= 4) ? openDt.substring(0, 4) : null;

        return this.webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path("/search/movie")
                            .queryParam("api_key", apikey)
                            .queryParam("query", query)
                            .queryParam("language", "ko-KR");
                    if (year != null) {
                        uriBuilder.queryParam("year", year);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(String.class)
                .map(this::converToJsonNode);
    }

    /**
     * tmdb - kobis 영화 동일성 확인
     * titleSim + dateSim 계산 -> overallSIm
     * 가장 높은 highestSim을 가진 영화 반환
     */
    private JsonNode findMostSimilarMovie(JsonNode jsonNode, String title, String openDt) {
        if (jsonNode.has("results")) {
            JsonNode results = jsonNode.get("results");
            JsonNode mostSimilar = null;
            double highestSim = 0;

            for (JsonNode movie : results) {
                double titleSim = calculateTitleSim(title, movie.path("title").asText());
                double dateSim = calculateDateSim(openDt, movie.path("release_date").asText());
                
                double overallSim = (titleSim * 0.6) + (dateSim * 0.4);
                
                if (overallSim > highestSim) {
                    highestSim = overallSim;
                    mostSimilar = movie;
                }
            }
            return mostSimilar;
        }
        return null;
    }

    /**
     * 제목 유사도 계산
     * Levenshtein Distance로 문자열 계산
     */
    private double calculateTitleSim(String s1, String s2) {
        int maxLen = Math.max(s1.length(), s2.length());
        if(maxLen > 0) {
            return (maxLen - calculateLevenshteinDistance(s1, s2)) / (double) maxLen;
        }
        return 1.0;
    }

    /**
     * 날짜 유사도 계산
     * 두 개봉일 차이 계산 후 차이 적을수록 높은 가중치
     */
    private double calculateDateSim(String date1, String date2) {
        LocalDate d1 = LocalDate.parse(date1);
        LocalDate d2 = LocalDate.parse(date2);

        long dayBetween = Math.abs(ChronoUnit.DAYS.between(d1, d2));
        return Math.max(0, 1 - (dayBetween/30.0));
    }

    /**
     * Levenshtein Distance 계산
     */
    private int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for(int i=0; i<=s1.length(); i++) {
            for(int j=0; j<=s2.length(); j++) {
                if (i==0) {
                    dp[i][j] = j;
                }
                else if(j==0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i-1][j-1] +
                            costOfSubstitution(s1.charAt(i-1), s2.charAt(j-1)),
                            dp[i-1][j] + 1,
                            dp[i][j-1] + 1);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private int min(int... numbers) {
        return Arrays.stream(numbers)
                .min()
                .orElse(Integer.MAX_VALUE);
    }

    /**
     * String json을 JonNode로 변환
     */
    private JsonNode converToJsonNode(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON string to JsonNode", e);
        }
    }


    /**
     * 해당 movieId의 영화 상세정보 가져오기
     */
    public Mono<String> getMovieDetails(Long movieId) {
        return webClient.get()
                .uri("/movie/" + movieId + "?api_key={api_key}&language=ko-KR&append_to_response=credits", apikey)
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * 해당 personId의 인물 상세정보 가져오기
     */
//    @Cacheable(value = "personDetails", key = "#personId")
    public Mono<String> getPersonDetails(Long personId) {
        return webClient.get()
                .uri("/person/" + personId + "/combined_credits?api_key={api_key}&language=ko-KR", apikey)
                .retrieve()
                .bodyToMono(String.class);
    }

    /**
     * 장르 정보 가져오기
     */
    public Mono<String> getGenres() {
        return webClient.get()
                .uri("/genre/movie/list?api_key={api_key}&language=ko-KR", apikey)
                .retrieve()
                .bodyToMono(String.class);
    }

//    @Cacheable(value = "movieProvider", key = "#movieTId")
    public Mono<String> getMovieProvider(Long movieTId) {
        return webClient.get()
                .uri("/movie/{movieId}/watch/providers?api_key={api_key}", movieTId, apikey)
                .retrieve()
                .bodyToMono(String.class);
    }

}