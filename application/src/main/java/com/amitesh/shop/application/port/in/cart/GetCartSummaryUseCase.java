package com.amitesh.shop.application.port.in.cart;

import com.amitesh.shop.model.cart.CartSummary;
import com.amitesh.shop.model.customer.CustomerId;

public interface GetCartSummaryUseCase {

  CartSummary getCartSummary(CustomerId customerId);
}
