package com.arhohuttunen.coffeeshop.application.order;

import com.arhohuttunen.coffeeshop.shared.Drink;
import com.arhohuttunen.coffeeshop.shared.Location;
import com.arhohuttunen.coffeeshop.shared.Milk;
import com.arhohuttunen.coffeeshop.shared.Size;
import com.arhohuttunen.coffeeshop.shared.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderStateTransitionTest {

    // ===== PATTERN AAA (Arrange, Act, Assert) =====
    // 1. ARRANGE: Preparare i dati
    // 2. ACT: Eseguire l'azione
    // 3. ASSERT: Verificare il risultato

    // Helper method per creare un ordine valido
    private Order createValidOrder() {
        return new Order(Location.TAKE_AWAY, List.of(
                new LineItem(Drink.LATTE, Milk.WHOLE, Size.SMALL, 1)
        ));
    }

    // ===== TEST 1: Ordine nasce nello stato PAYMENT_EXPECTED =====
    @Test
    void newOrderStartsInPaymentExpectedStatus() {
        // ARRANGE
        var order = createValidOrder();

        // ACT & ASSERT
        assertThat(order.getStatus()).isEqualTo(Status.PAYMENT_EXPECTED);
    }

    // ===== TEST 2: Transizione valida PAYMENT_EXPECTED -> PAID =====
    @Test
    void canTransitionFromPaymentExpectedToPaid() {
        // ARRANGE
        var order = createValidOrder();

        // ACT
        var result = order.markPaid();

        // ASSERT che lo stato cambia
        assertThat(order.getStatus()).isEqualTo(Status.PAID);
        // ASSERT che il metodo ritorna se stesso (fluent interface)
        assertThat(result).isSameAs(order);
    }

    // ===== TEST 3: NON puoi pagare due volte =====
    @Test
    void cannotMarkAsPaidTwice() {
        // ARRANGE
        var order = createValidOrder();
        order.markPaid();  // Prima volta OK

        // ACT & ASSERT - La seconda volta deve lancia eccezione
        assertThatThrownBy(order::markPaid)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Order is already paid");
    }

    // ===== TEST 4: NON puoi preparare se non hai pagato =====
    @Test
    void cannotMarkBeingPreparedWithoutPaying() {
        // ARRANGE
        var order = createValidOrder();  // Stato: PAYMENT_EXPECTED

        // ACT & ASSERT
        assertThatThrownBy(order::markBeingPrepared)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Order is not paid");
    }

    // ===== TEST 5: Sequenza completa valida =====
    @Test
    void validFullStateTransitionSequence() {
        // ARRANGE
        var order = createValidOrder();

        // ACT & ASSERT - seguono la catena di transizioni
        assertThat(order.markPaid().getStatus()).isEqualTo(Status.PAID);
        assertThat(order.markBeingPrepared().getStatus()).isEqualTo(Status.PREPARING);
        assertThat(order.markPrepared().getStatus()).isEqualTo(Status.READY);
        assertThat(order.markTaken().getStatus()).isEqualTo(Status.TAKEN);
    }

    // ===== TEST 6: NON puoi saltare stati =====
    @Test
    void cannotMarkPreparedWithoutBeingPrepared() {
        // ARRANGE
        var order = createValidOrder();
        order.markPaid();  // PAID state

        // ACT & ASSERT - Prova a saltare direttamente a READY
        assertThatThrownBy(order::markPrepared)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Order is not being prepared");
    }

    // ===== TEST 7: Puoi cancellare solo prima di pagare =====
    @Test
    void canBeCancelledOnlyInPaymentExpectedStatus() {
        // ARRANGE & ACT & ASSERT
        var order = createValidOrder();
        assertThat(order.canBeCancelled()).isTrue();  // State: PAYMENT_EXPECTED

        // ACT
        order.markPaid();

        // ASSERT
        assertThat(order.canBeCancelled()).isFalse();  // State: PAID
    }

}
