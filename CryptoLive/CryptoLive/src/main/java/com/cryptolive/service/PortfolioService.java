package com.cryptolive.service;

import com.cryptolive.model.PortfolioItem;
import com.cryptolive.model.User;
import com.cryptolive.repository.PortfolioRepository;
import com.cryptolive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebClient coinGeckoClient;

    public List<PortfolioItem> getPortfolioByUser(String email) {
        return portfolioRepository.findByUserEmail(email);
    }

    public PortfolioItem addCoin(String email, String coinId, BigDecimal quantity) {
        PortfolioItem item = new PortfolioItem();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        item.setUser(user);
        item.setCoinId(coinId);
        item.setQuantity(quantity);
        portfolioRepository.save(item);
        return item;
    }

    public PortfolioItem updateCoin(String email, String coinId, BigDecimal quantity) {
        PortfolioItem item = portfolioRepository.findByUserEmailAndCoinId(email, coinId);
        if (item == null) {
            throw new RuntimeException("Criptomoneda no encontrada en el portafolio");
        }
        item.setQuantity(quantity);
        portfolioRepository.save(item);
        return item;
    }

    public void deleteCoin(String email, String coinId) {
        PortfolioItem item = portfolioRepository.findByUserEmailAndCoinId(email, coinId);
        if (item == null) {
            throw new RuntimeException("Criptomoneda no encontrada en el portafolio");
        }
        portfolioRepository.delete(item);
    }

    public Map<String, BigDecimal> getCryptoPrices(List<String> coinIds) {
        String ids = String.join(",", coinIds);
        String url = "/simple/price?ids=" + ids + "&vs_currencies=usd";
        return coinGeckoClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}