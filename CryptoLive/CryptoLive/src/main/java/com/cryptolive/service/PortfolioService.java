package com.cryptolive.service;

import com.cryptolive.model.PortfolioItem;
import com.cryptolive.model.User;
import com.cryptolive.repository.PortfolioRepository;
import com.cryptolive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


//Contien la logica de negocio para gestionar el portafolio de criptomonedas de los usuarios ahce un CRUD
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

        // Obtener el precio actual de la moneda
        Map<String, BigDecimal> prices = getCryptoPrices(List.of(coinId));
        BigDecimal price = prices.get(coinId);
        if (price != null) {
            item.setLastPrice(price);
            item.setLastUpdated(java.time.LocalDateTime.now());
        }

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

    //Lamada ala API de coingeko para consultar los precios de los activos en el portafolio
    public Map<String, BigDecimal> getCryptoPrices(List<String> coinIds) {
        String ids = String.join(",", coinIds);
        String url = "/simple/price?ids=" + ids + "&vs_currencies=usd";

        Map<String, Map<String, BigDecimal>> response = coinGeckoClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Map<String, BigDecimal>>>() {})
                .block();

        // Extraer los precios de la estructura anidada
        Map<String, BigDecimal> prices = new HashMap<>();
        response.forEach((coinId, priceData) -> {
            BigDecimal price = priceData.get("usd");
            if (price != null) {
                prices.put(coinId, price);
            }
        });

        return prices;
    }


    public Map<String, Object> getPortfolioResponse(String email) {
        List<PortfolioItem> portfolioItems = this.getPortfolioByUser(email);

        //Cuando no hay monedas en el portafolio
        if (portfolioItems.isEmpty()) {
            throw new RuntimeException("El portafolio está vacío");
        }

        BigDecimal saldoActual = calculateSaldoActual(portfolioItems);
        List<Map<String, Object>> monedas = buildMonedasResponse(portfolioItems);

        Map<String, Object> response = new HashMap<>();
        response.put("saldoActual", saldoActual);
        response.put("monedas", monedas);

        return response;
    }

    // Calcula el saldo actual del portafolio sumando el valor de cada moneda
    private BigDecimal calculateSaldoActual(List<PortfolioItem> portfolioItems) {
        return portfolioItems.stream()
                .map(item -> item.getQuantity().multiply(item.getLastPrice() != null ? item.getLastPrice() : BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Construye la respuesta de las monedas en el portafolio
    private List<Map<String, Object>> buildMonedasResponse(List<PortfolioItem> portfolioItems) {
        return portfolioItems.stream().map(item -> {
            Map<String, Object> moneda = new HashMap<>();
            moneda.put("coinId", item.getCoinId());
            moneda.put("quantity", item.getQuantity());
            moneda.put("lastPrice", item.getLastPrice());
            moneda.put("lastUpdated", item.getLastUpdated());
            return moneda;
        }).collect(Collectors.toList());
    }
}