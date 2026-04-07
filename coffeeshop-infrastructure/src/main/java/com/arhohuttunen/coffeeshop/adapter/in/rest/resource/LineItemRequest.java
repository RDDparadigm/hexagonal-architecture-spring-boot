package com.arhohuttunen.coffeeshop.adapter.in.rest.resource;

import com.arhohuttunen.coffeeshop.application.order.LineItem;
import com.arhohuttunen.coffeeshop.shared.Drink;
import com.arhohuttunen.coffeeshop.shared.Milk;
import com.arhohuttunen.coffeeshop.shared.Size;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LineItemRequest(@NotNull @NotEmpty Drink drink, @NotNull @NotEmpty Milk milk, @NotNull @NotEmpty Size size, @NotNull @NotEmpty Integer quantity) {
    public LineItem toDomain() {
        return new LineItem(drink, milk, size, quantity);
    }
}
