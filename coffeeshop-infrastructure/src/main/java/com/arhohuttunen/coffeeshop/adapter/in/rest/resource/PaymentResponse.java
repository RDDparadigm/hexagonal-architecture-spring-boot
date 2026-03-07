package com.arhohuttunen.coffeeshop.adapter.in.rest.resource;

import com.arhohuttunen.coffeeshop.application.payment.Payment;

import java.time.YearMonth;

public record PaymentResponse(String cardHolderName, String cardNumber, YearMonth expiry) {
    public static PaymentResponse fromDomain(Payment payment) {
        var creditCard = payment.creditCard();
        return new PaymentResponse(
                creditCard.cardHolderName(),
                creditCard.cardNumber(),
                creditCard.expiry()
        );
    }
}
