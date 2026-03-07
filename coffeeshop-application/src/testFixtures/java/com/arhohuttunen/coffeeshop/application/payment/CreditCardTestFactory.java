package com.arhohuttunen.coffeeshop.application.payment;

import java.time.Month;
import java.time.YearMonth;

public class CreditCardTestFactory {
    public static CreditCard aCreditCard() {
        return new CreditCard("Michael Faraday", "11223344", YearMonth.of(2023, Month.JANUARY));
    }
}
