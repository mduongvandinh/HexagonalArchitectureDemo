package com.amitesh.shop.application.service.cart;

import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_CUSTOMER_ID;
import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amitesh.shop.application.port.out.persistence.CartRepository;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.CartSummary;
import com.amitesh.shop.model.cart.InsufficientStockException;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
class GetCartSummaryServiceTest {

  @Mock private CartRepository cartRepository;

  @InjectMocks private GetCartSummaryService getCartSummaryService;

  @InjectSoftAssertions private SoftAssertions softly;

  @Test
  void testGetCartSummary_invalidCustomerId_throwsException() {
    assertThatThrownBy(() -> getCartSummaryService.getCartSummary(null))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'customerId' must not be null");

    verify(cartRepository, never()).findByCustomerId(any());
  }

  @Test
  void testGetCartSummary_cartWithProducts_returnsCorrectSummary()
      throws InsufficientStockException {
    Cart persistedCart = new Cart(TEST_CUSTOMER_ID);
    persistedCart.addProduct(TEST_PRODUCT_1, 3);

    when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID))
        .thenReturn(Optional.of(persistedCart));

    CartSummary summary = getCartSummaryService.getCartSummary(TEST_CUSTOMER_ID);

    softly.assertThat(summary.customerId()).isEqualTo(TEST_CUSTOMER_ID);
    softly.assertThat(summary.numberOfItems()).isEqualTo(3);
    softly.assertThat(summary.empty()).isFalse();
    softly.assertThat(summary.subTotal()).isEqualTo(TEST_PRODUCT_1.price().multiply(3));

    verify(cartRepository).findByCustomerId(TEST_CUSTOMER_ID);
  }

  @Test
  void testGetCartSummary_noPersistedCart_returnsEmptySummary() {
    when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID)).thenReturn(Optional.empty());

    CartSummary summary = getCartSummaryService.getCartSummary(TEST_CUSTOMER_ID);

    softly.assertThat(summary.customerId()).isEqualTo(TEST_CUSTOMER_ID);
    softly.assertThat(summary.numberOfItems()).isZero();
    softly.assertThat(summary.empty()).isTrue();

    verify(cartRepository).findByCustomerId(TEST_CUSTOMER_ID);
  }
}
