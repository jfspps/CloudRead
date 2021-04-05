package com.example.cloudread.service;

import com.example.JAXBmodel.FundamentalPieceList;
import com.example.cloudread.config.WebClientConfig;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FundamentalPieceService {

    private final WebClientConfig webClientConfig;

    public FundamentalPieceService(WebClientConfig webClientConfig) {
        this.webClientConfig = webClientConfig;
    }

    public FundamentalPieceList getAllPieces_Adv(){
        return webClientConfig.webClientWithTimeout()
                .get()
                .uri(String.join("", "/fundamentals/"))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("4xx: client error")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("5xx: server is not responding")))
                .bodyToMono(FundamentalPieceList.class)
                .block();
    }

    public FundamentalPieceList getAllPieces_simple(){
        return webClientConfig.webClientSimple()
                .get()
                .uri(String.join("", "/fundamentals/"))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("4xx: client error")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("5xx: server is not responding")))
                .bodyToMono(FundamentalPieceList.class)
                .block();
    }
}
