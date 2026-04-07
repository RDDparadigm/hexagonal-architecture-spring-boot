package com.arhohuttunen.coffeeshop.application.order;

import com.arhohuttunen.coffeeshop.shared.Location;
import com.arhohuttunen.coffeeshop.shared.Status;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Order {
    private UUID id = UUID.randomUUID();
    private final Location location;
    private final List<LineItem> items;
    private Status status = Status.PAYMENT_EXPECTED;

    // singleton pattern to prevent multiple instances of OrderTransition
    private static final OrderTransition TRANSITION = new OrderTransition();

    public Order(Location location, List<LineItem> items) {

        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.items = Objects.requireNonNull(items, "Items cannot be null");

        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
    }

    public Order(UUID id, Location location, List<LineItem> items, Status status) {

        this.id = Objects.requireNonNull(id, "Id cannot be null");
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.items = Objects.requireNonNull(items, "Items cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");

        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

    }

    public UUID getId() {
        return this.id;
    }

    public Location getLocation() {
        return this.location;
    }

    public List<LineItem> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    public Status getStatus() {
        return this.status;
    }

    public boolean canBeCancelled() {
        return status == Status.PAYMENT_EXPECTED;
    }

    public BigDecimal getCost() {
        return items.stream().map(LineItem::getCost).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getCostWithDiscount(int discountPercent) {

        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }

        BigDecimal originalCost = getCost();
        BigDecimal discount = originalCost.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal
                .valueOf(100), 2, RoundingMode.HALF_DOWN);

        return originalCost.subtract(discount);

    }

    public Order update(Location location, List<LineItem> items) {
        if (status == Status.PAID) {
            throw new IllegalStateException("Order is already paid");
        }
        return new Order(id, location, items, status);
    }

    public Order markPaid() {
        if (status != Status.PAYMENT_EXPECTED) {
            throw new IllegalStateException("Order is already paid");
        }
        status = Status.PAID;
        return this;
    }

    public Order markBeingPrepared() {
        if (status != Status.PAID) {
            throw new IllegalStateException("Order is not paid");
        }
        status = Status.PREPARING;
        return this;
    }

    public Order markPrepared() {
        if (status != Status.PREPARING) {
            throw new IllegalStateException("Order is not being prepared");
        }
        status = Status.READY;
        return this;
    }

    public Order markTaken() {
        if (status != Status.READY) {
            throw new IllegalStateException("Order is not ready");
        }
        status = Status.TAKEN;
        return this;
    }
}
