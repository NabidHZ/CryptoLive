package com.cryptolive.controller;

import com.cryptolive.model.PortfolioItem;
import com.cryptolive.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    public List<PortfolioItem> getPortfolio(Principal principal) {
        return portfolioService.getPortfolioByUser(principal.getName());
    }

    @PostMapping("/coins")
    public PortfolioItem addCoin(@RequestBody PortfolioItem item, Principal principal) {
        return portfolioService.addCoin(principal.getName(), item.getCoinId(), item.getQuantity());
    }

    @PutMapping("/coins/{coinId}")
    public PortfolioItem updateCoin(@PathVariable String coinId, @RequestBody PortfolioItem item, Principal principal) {
        return portfolioService.updateCoin(principal.getName(), coinId, item.getQuantity());
    }

    @DeleteMapping("/coins/{coinId}")
    public void deleteCoin(@PathVariable String coinId, Principal principal) {
        portfolioService.deleteCoin(principal.getName(), coinId);
    }
}