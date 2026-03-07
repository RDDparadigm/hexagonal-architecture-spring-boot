package com.arhohuttunen.coffeeshop.adapter.in.rest.resource;

import jakarta.validation.constraints.NotNull;

import java.time.YearMonth;

public record PaymentRequest(
        @NotNull String cardHolderName,
        @NotNull String cardNumber,
        @NotNull YearMonth expiry) {
}
