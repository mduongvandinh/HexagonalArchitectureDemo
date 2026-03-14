package com.amitesh.shop.model.cart;

import static com.amitesh.shop.model.ModelTestHelper.PRODUCT_QUANTITY_IN_STOCK;
import static com.amitesh.shop.model.ModelTestHelper.createTestProduct;
import static com.amitesh.shop.model.ModelTestHelper.emptyCartForRandomCustomer;
import static com.amitesh.shop.model.ModelTestHelper.euros;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.amitesh.shop.model.product.Product;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
class CartSummaryTest {

  @InjectSoftAssertions private SoftAssertions softly;

  @Test
  void fromCart_givenEmptyCart_returnsEmptySummary() {
    Cart cart = emptyCartForRandomCustomer();

    CartSummary summary = CartSummary.fromCart(cart);

    softly.assertThat(summary.customerId()).isEqualTo(cart.id());
    softly.assertThat(summary.numberOfItems()).isZero();
    softly.assertThat(summary.subTotal()).isNull();
    softly.assertThat(summary.empty()).isTrue();
  }

  @Test
  void fromCart_givenCartWithProducts_returnsCorrectSummary() throws InsufficientStockException {
    Cart cart = emptyCartForRandomCustomer();
    Product product = createTestProduct(euros(10, 0), PRODUCT_QUANTITY_IN_STOCK);
    cart.addProduct(product, 3);

    CartSummary summary = CartSummary.fromCart(cart);

    softly.assertThat(summary.customerId()).isEqualTo(cart.id());
    softly.assertThat(summary.numberOfItems()).isEqualTo(3);
    softly.assertThat(summary.subTotal()).isEqualTo(euros(30, 0));
    softly.assertThat(summary.empty()).isFalse();
  }

  @Test
  void fromCart_givenNull_throwsException() {
    assertThatIllegalArgumentException().isThrownBy(() -> CartSummary.fromCart(null));
  }
}
