// src/main/java/com/cryptolive/service/CoingekoService.java
package com.cryptolive.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class CoingekoService {

    private final WebClient coinGeckoClient;

    public CoingekoService(@Qualifier("coinGeckoClient") WebClient coinGeckoClient) {
        this.coinGeckoClient = coinGeckoClient;
    }

    public Mono<Map<String, Map<String, Double>>> getSimplePrices(List<String> coinIds) {
        String ids = String.join(",", coinIds);
        return coinGeckoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/simple/price")
                        .queryParam("ids", ids)
                        .queryParam("vs_currencies", "usd")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}