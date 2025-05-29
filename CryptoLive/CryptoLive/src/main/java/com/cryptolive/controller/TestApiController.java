package com.cryptolive.controller;

import com.cryptolive.service.CoingekoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
public class TestApiController {

    @Autowired
    private CoingekoService coingekoService;

    @GetMapping("/api/test-coingecko")
    public Mono<Map<String, Map<String, Double>>> testCoingecko() {
        return coingekoService.getSimplePrices(List.of("bitcoin", "ethereum"));
    }
}