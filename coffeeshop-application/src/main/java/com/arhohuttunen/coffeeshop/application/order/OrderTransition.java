package com.arhohuttunen.coffeeshop.application.order;

import com.arhohuttunen.coffeeshop.shared.Status;

public class OrderTransition {
    
    public enum OrderEvent {
        TO_PAID(Status.PAYMENT_EXPECTED, Status.PAID, "Order is already paid"),
        TO_PREPARING(Status.PAID, Status.PREPARING, "Order is not paid"),
        TO_READY(Status.PREPARING, Status.READY, "Order is not being prepared"),
        TO_TAKEN(Status.READY, Status.TAKEN, "Order is not ready");

        private final Status from;
        private final Status to;
        private final String errorMessage;

        OrderEvent(Status from, Status to, String errorMessage) {
            this.from = from;
            this.to = to;
            this.errorMessage = errorMessage;
        }
    }

    protected Boolean canTransition(Order order, OrderEvent event) {
        return order.getStatus() == event.from;
    }

    protected Status getNextStatus(OrderEvent event) {
        return event.to;
    }

    protected String getErrorMessage(OrderEvent event) {
        return event.errorMessage;
    }

}
