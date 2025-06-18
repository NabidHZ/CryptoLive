package com.cryptolive.controller;

import com.cryptolive.model.PortfolioItem;
import com.cryptolive.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    // Endpoint para obtener el portafolio de un usuario
    @PostMapping
    public ResponseEntity<?> getPortfolio(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("El email es obligatorio");
        }
        try {
            Map<String, Object> response = portfolioService.getPortfolioResponse(email);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para añadir una moneda al portafolio
    @PostMapping("/coins")
    public ResponseEntity<?> addCoin(@RequestBody Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        String coinId = (String) requestBody.get("coinId");
        BigDecimal quantity;

        try {
            quantity = new BigDecimal(requestBody.get("quantity").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("La cantidad debe ser un número válido");
        }

        if (email == null || email.isEmpty() || coinId == null || coinId.isEmpty() || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Datos inválidos");
        }

        try {
            PortfolioItem addedCoin = portfolioService.addCoin(email, coinId, quantity);
            return ResponseEntity.ok("Moneda añadida correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para actualizar la cantidad de una moneda en el portafolio
    @PutMapping("/coins")
    public ResponseEntity<?> updateCoin(@RequestBody Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        String coinId = (String) requestBody.get("coinId");
        BigDecimal quantity;

        try {
            quantity = new BigDecimal(requestBody.get("quantity").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("La cantidad debe ser un número válido");
        }

        if (email == null || email.isEmpty() || coinId == null || coinId.isEmpty() || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Datos inválidos");
        }

        try {
            portfolioService.updateCoin(email, coinId, quantity);
            return ResponseEntity.ok("Moneda actualizada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para eliminar una moneda del portafolio
    @DeleteMapping("/coins")
    public ResponseEntity<?> deleteCoin(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String coinId = requestBody.get("coinId");

        if (email == null || email.isEmpty() || coinId == null || coinId.isEmpty()) {
            return ResponseEntity.badRequest().body("Datos inválidos");
        }

        try {
            portfolioService.deleteCoin(email, coinId);
            return ResponseEntity.ok("Moneda eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}