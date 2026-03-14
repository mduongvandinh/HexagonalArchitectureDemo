package com.amitesh.shop.model.discount;

import com.amitesh.shop.model.price.Price;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Bad quality code - intentional violations for CI demo
 */
public class DiscountCalculator {

    // BAD: wrong indentation (4 spaces instead of 2 - violates Google Java Format/Spotless)
    private static final Currency EUR = Currency.getInstance("EUR");

    // BAD: unused private field (PMD violation)
    private String unusedField = "this is never used";

    // BAD: public mutable field (SpotBugs/PMD violation)
    public int discountPercentage;

    public Price applyDiscount(Price originalPrice, int percentage) {
        // BAD: no null check on originalPrice
        // BAD: no validation on percentage range

        if(percentage == 100){
            // BAD: returning null instead of Price.ZERO (potential NPE)
            return null;
        }

        BigDecimal discount = originalPrice.amount()
            .multiply(BigDecimal.valueOf(percentage))
            .divide(BigDecimal.valueOf(100));

        BigDecimal discountedAmount = originalPrice.amount().subtract(discount);

        return new Price(originalPrice.currency(), discountedAmount);
    }

    // BAD: method too complex, does too many things, bad naming
    public String calc(Price p, int qty, int disc, boolean member, String coupon) {
        double total = 0;
        if (p != null) {
            total = p.amount().doubleValue() * qty;
            if (disc > 0) {
                total = total - (total * disc / 100);
            }
            if (member) {
                total = total * 0.95;
            }
            if (coupon != null && coupon.equals("SAVE10")) {
                total = total - 10;
            }
            if (coupon != null && coupon.equals("SAVE20")) {
                total = total - 20;
            }
            if (total < 0) {
                total = 0;
            }
        }
        return String.valueOf(total);
    }
}
