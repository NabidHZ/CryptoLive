package com.cryptolive.controller;

import com.cryptolive.service.CryptoMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market")
public class CryptoMarketController {

    @Autowired
    private CryptoMarketService cryptoMarketService;

    @GetMapping("/top20")
    public ResponseEntity<List<Map<String, Object>>> getTop20Coins() {
        List<Map<String, Object>> coins = cryptoMarketService.getTop20Coins();
        return ResponseEntity.ok(coins);
    }
}