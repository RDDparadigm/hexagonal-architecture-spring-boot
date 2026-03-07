package com.arhohuttunen.coffeeshop.application.payment;

import java.time.YearMonth;

public record CreditCard(String cardHolderName, String cardNumber, YearMonth expiry) { }
