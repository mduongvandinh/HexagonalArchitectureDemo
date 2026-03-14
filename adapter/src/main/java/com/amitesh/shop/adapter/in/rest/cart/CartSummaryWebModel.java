package com.amitesh.shop.adapter.in.rest.cart;

import com.amitesh.shop.model.cart.CartSummary;
import com.amitesh.shop.model.price.Price;

public record CartSummaryWebModel(int numberOfItems, Price subTotal, boolean empty) {

  public static CartSummaryWebModel fromDomainModel(final CartSummary summary) {
    return new CartSummaryWebModel(
        summary.numberOfItems(), summary.subTotal(), summary.empty());
  }
}
