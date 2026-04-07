package com.arhohuttunen.coffeeshop.adapter.in.rest.resource;

import com.arhohuttunen.coffeeshop.application.order.LineItem;
import com.arhohuttunen.coffeeshop.shared.Location;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(@NotNull @NotEmpty Location location, @NotNull @NotEmpty List<LineItemRequest> items) {
    public List<LineItem> toDomainItems() {
        return items.stream().map(LineItemRequest::toDomain).toList();
    }
}
