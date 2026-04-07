package com.arhohuttunen.coffeeshop.adapter.in.rest.resource;

import com.arhohuttunen.coffeeshop.application.order.LineItem;
import com.arhohuttunen.coffeeshop.shared.Drink;
import com.arhohuttunen.coffeeshop.shared.Milk;
import com.arhohuttunen.coffeeshop.shared.Size;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LineItemRequest(@NotNull Drink drink, @NotNull Milk milk, @NotNull Size size, @Positive @NotNull Integer quantity) {
    public LineItem toDomain() {
        return new LineItem(drink, milk, size, quantity);
    }
}
