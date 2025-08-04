// CryptoLive/CryptoLive/src/main/java/com/cryptolive/service/CryptoMarketService.java
package com.cryptolive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class CryptoMarketService {

    @Autowired
    private WebClient coinGeckoClient;

    public List<Map<String, Object>> getTop20Coins() {
        return coinGeckoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/coins/markets")
                        .queryParam("vs_currency", "usd")
                        .queryParam("order", "market_cap_desc")
                        .queryParam("per_page", 20)
                        .queryParam("page", 1)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();
    }
}