package com.arhohuttunen.coffeeshop.adapter.in.rest.resource;

import java.time.YearMonth;

public record PaymentRequest(
        String cardHolderName,
        String cardNumber,
        YearMonth expiry) {
}
