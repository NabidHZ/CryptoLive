// src/main/java/com/cryptolive/service/PriceBroadcastService.java
package com.cryptolive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceBroadcastService {

    private final CoingekoService coinService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public PriceBroadcastService(CoingekoService coinService, SimpMessagingTemplate messagingTemplate) {
        this.coinService = coinService;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 30_000)
    public void fetchAndBroadcast() {
        coinService.getSimplePrices(List.of("bitcoin", "ethereum"))
                .subscribe(prices ->
                        messagingTemplate.convertAndSend("/topic/prices", prices)
                );
    }
}