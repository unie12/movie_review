//package com.example.movie_review.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.ExchangeStrategies;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Configuration
//public class WebclientConfig {
//
//    @Bean
//    public WebClient webClient() {
//        return WebClient.builder()
//                .exchangeStrategies(ExchangeStrategies.builder()
//                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
//                    .build())
//        .build();
//    }
//}
