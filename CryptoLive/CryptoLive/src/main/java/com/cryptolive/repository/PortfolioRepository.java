package com.cryptolive.repository;

import com.cryptolive.model.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<PortfolioItem, Long> {
    List<PortfolioItem> findByUserEmail(String email);

    @Query("SELECT DISTINCT p.coinId FROM PortfolioItem p")
    List<String> findDistinctCoinIds();

    List<PortfolioItem> findAllByCoinIdIn(List<String> coinIds);
    PortfolioItem findByUserEmailAndCoinId(String email, String coinId);
}