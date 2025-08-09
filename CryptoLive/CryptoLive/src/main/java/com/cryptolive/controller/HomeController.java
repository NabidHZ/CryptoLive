// src/main/java/com/cryptolive/controller/HomeController.java
package com.cryptolive.controller;

import com.cryptolive.service.CryptoMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private CryptoMarketService cryptoMarketService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("top20", cryptoMarketService.getTop20Coins());
        return "index";
    }
}