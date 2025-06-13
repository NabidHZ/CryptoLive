package com.cryptolive.controller;

import com.cryptolive.model.PortfolioItem;
import com.cryptolive.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<?> getPortfolio(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        List<PortfolioItem> portfolioItems = portfolioService.getPortfolioByUser(email);

        // Calcular el saldo actual sumando (quantity * lastPrice) de cada moneda
        BigDecimal saldoActual = portfolioItems.stream()
                .map(item -> item.getQuantity().multiply(item.getLastPrice() != null ? item.getLastPrice() : BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Construir la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("saldoActual", saldoActual);
        response.put("monedas", portfolioItems.stream().map(item -> {
            Map<String, Object> moneda = new HashMap<>();
            moneda.put("coinId", item.getCoinId());
            moneda.put("quantity", item.getQuantity());
            moneda.put("lastPrice", item.getLastPrice());
            moneda.put("lastUpdated", item.getLastUpdated());
            return moneda;
        }).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }
}