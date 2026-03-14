package com.amitesh.shop.model.discount;

import static org.assertj.core.api.Assertions.assertThat;

import com.amitesh.shop.model.price.Price;
import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class DiscountCalculatorTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private final DiscountCalculator calculator = new DiscountCalculator();

    @Test
    void applyDiscount_10percent_reducesPrice() {
        Price original = new Price(EUR, BigDecimal.valueOf(100).setScale(2));

        Price result = calculator.applyDiscount(original, 10);

        // BAD: This test will FAIL - wrong expected value
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(80).setScale(2));
    }

    @Test
    void applyDiscount_100percent_returnsFree() {
        Price original = new Price(EUR, BigDecimal.valueOf(50).setScale(2));

        Price result = calculator.applyDiscount(original, 100);

        // BAD: This will cause NPE because applyDiscount returns null for 100%
        assertThat(result.amount()).isEqualTo(BigDecimal.ZERO);
    }
}
