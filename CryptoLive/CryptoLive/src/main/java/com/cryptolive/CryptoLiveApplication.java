// src/main/java/com/cryptolive/CryptoLiveApplication.java
package com.cryptolive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoLiveApplication {
	public static void main(String[] args) {

		SpringApplication.run(CryptoLiveApplication.class, args);
	}
}