package com.cryptolive.service;

import com.cryptolive.model.PortfolioItem;
import com.cryptolive.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class PriceUpdateService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioService portfolioService;

    @Scheduled(fixedRate = 30000)
    public void refreshPrices() {
        System.out.println("Ejecutando refreshPrices..."); // Registro para verificar ejecución
        List<String> coinIds = portfolioRepository.findDistinctCoinIds();
        System.out.println("Coin IDs encontrados: " + coinIds); // Registro para depuración

        Map<String, BigDecimal> prices = portfolioService.getCryptoPrices(coinIds);
        System.out.println("Precios obtenidos: " + prices); // Registro para depuración

        portfolioRepository.findAllByCoinIdIn(coinIds).forEach(item -> {
            BigDecimal price = prices.get(item.getCoinId());
            if (price != null) {
                item.setLastPrice(price);
                item.setLastUpdated(LocalDateTime.now());
                portfolioRepository.save(item);
                System.out.println("Actualizado: " + item.getCoinId() + " con precio " + price); // Registro para depuración
            }
        });
    }
}