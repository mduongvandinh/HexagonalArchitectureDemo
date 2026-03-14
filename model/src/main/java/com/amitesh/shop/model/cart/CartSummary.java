package com.amitesh.shop.model.cart;

import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.price.Price;

/**
 * Immutable summary of a shopping cart, useful for lightweight cart previews (e.g. header widget).
 */
public record CartSummary(CustomerId customerId, int numberOfItems, Price subTotal, boolean empty) {

  public static CartSummary fromCart(final Cart cart) {
    if (null == cart) {
      throw new IllegalArgumentException("'cart' must not be null");
    }
    return new CartSummary(cart.id(), cart.numberOfItems(), cart.subTotal(), cart.isEmpty());
  }
}
