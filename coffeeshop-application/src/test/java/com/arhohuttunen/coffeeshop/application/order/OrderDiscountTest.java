package com.arhohuttunen.coffeeshop.application.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.arhohuttunen.coffeeshop.shared.Drink;
import com.arhohuttunen.coffeeshop.shared.Location;
import com.arhohuttunen.coffeeshop.shared.Milk;
import com.arhohuttunen.coffeeshop.shared.Size;

public class OrderDiscountTest {
    
    private Order createValidOrder() {
        return new Order(Location.TAKE_AWAY, List.of(
                new LineItem(Drink.LATTE, Milk.WHOLE, Size.SMALL, 1)  // Cost: 4.0
        ));
    }

    // Test more cases for discount calculation: 0%, 10%, 50%, 100%
    // the placeholder in the test name {0} is replaced by the first argument (discount), 
    // {1} by the second (original cost) and {2} by the third (expected cost)
    @ParameterizedTest(name = "Discount {0}% on cost {1} should be {2}")
    @MethodSource("discountCases")
    void testDiscountCalculation(int discount, BigDecimal originalCost, BigDecimal expectedCost) {

        var order = createValidOrder();
        var discountedCost = order.getCostWithDiscount(discount);

        assertThat(discountedCost)
            .usingComparator(BigDecimal::compareTo)  // This allows us to compare BigDecimals without worrying about scale (e.g., 3.60 vs 3.6)
            .isEqualTo(expectedCost);

    }  

    // Source method: fornisce i dati per il test parametrizzato
    private static Stream<Arguments> discountCases() {
        return Stream.of(
            arguments(0, BigDecimal.valueOf(4.0), BigDecimal.valueOf(4.0)),      // No discount
            arguments(10, BigDecimal.valueOf(4.0), BigDecimal.valueOf(3.6)),      // 10% off
            arguments(50, BigDecimal.valueOf(4.0), BigDecimal.valueOf(2.0)),      // 50% off
            arguments(100, BigDecimal.valueOf(4.0), BigDecimal.valueOf(0.0))      // 100% off
        );
    }

    @Test
    void discountMustBeBetween0And100() {
        var order = createValidOrder();

        assertThatThrownBy(() -> order.getCostWithDiscount(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount must be between 0 and 100");

        assertThatThrownBy(() -> order.getCostWithDiscount(101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount must be between 0 and 100");

    }
}
