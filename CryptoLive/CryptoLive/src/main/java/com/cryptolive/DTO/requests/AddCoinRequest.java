package com.cryptolive.DTO.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddCoinRequest {
    @NotNull
    @Email
    public String email;

    @NotNull
    public String coinId;

    @NotNull
    public BigDecimal quantity;
}