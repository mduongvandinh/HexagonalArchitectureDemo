package com.amitesh.shop.application.service.cart;

import com.amitesh.shop.application.port.in.cart.GetCartSummaryUseCase;
import com.amitesh.shop.application.port.out.persistence.CartRepository;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.CartSummary;
import com.amitesh.shop.model.customer.CustomerId;

public class GetCartSummaryService implements GetCartSummaryUseCase {

  private final CartRepository cartRepository;

  public GetCartSummaryService(CartRepository cartRepository) {
    this.cartRepository = cartRepository;
  }

  @Override
  public CartSummary getCartSummary(CustomerId customerId) {
    if (null == customerId) {
      throw new IllegalArgumentException("'customerId' must not be null");
    }

    Cart cart =
        cartRepository.findByCustomerId(customerId).orElseGet(() -> new Cart(customerId));

    return CartSummary.fromCart(cart);
  }
}
