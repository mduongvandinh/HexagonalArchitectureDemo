package com.amitesh.shop.adapter.in.rest.cart;

import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_CUSTOMER_ID;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_PRODUCT_1;
import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.assertThatResponseIsError;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.amitesh.shop.application.port.in.cart.GetCartSummaryUseCase;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.CartSummary;
import com.amitesh.shop.model.cart.InsufficientStockException;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class GetCartSummaryControllerTest {

  @LocalServerPort private Integer port;

  @MockBean private GetCartSummaryUseCase getCartSummaryUseCase;

  @Test
  void testGetCartSummary_givenInvalidCustomerId_returnsError() {
    String customerId = "foo";

    Response response =
        given().port(port).get("/carts/" + customerId + "/summary").then().extract().response();

    assertThatResponseIsError(response, BAD_REQUEST, "Invalid 'customerId'");
  }

  @Test
  void testGetCartSummary_givenValidCustomerId_returnsSummary()
      throws InsufficientStockException {
    Cart cart = new Cart(TEST_CUSTOMER_ID);
    cart.addProduct(TEST_PRODUCT_1, 3);
    CartSummary summary = CartSummary.fromCart(cart);

    when(getCartSummaryUseCase.getCartSummary(TEST_CUSTOMER_ID)).thenReturn(summary);

    Response response =
        given()
            .port(port)
            .get("/carts/" + TEST_CUSTOMER_ID.value() + "/summary")
            .then()
            .extract()
            .response();

    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.body().jsonPath().getInt("numberOfItems")).isEqualTo(3);
    assertThat(response.body().jsonPath().getBoolean("empty")).isFalse();
  }

  @Test
  void testGetCartSummary_givenEmptyCart_returnsEmptySummary() {
    CartSummary emptySummary = CartSummary.fromCart(new Cart(TEST_CUSTOMER_ID));

    when(getCartSummaryUseCase.getCartSummary(TEST_CUSTOMER_ID)).thenReturn(emptySummary);

    Response response =
        given()
            .port(port)
            .get("/carts/" + TEST_CUSTOMER_ID.value() + "/summary")
            .then()
            .extract()
            .response();

    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.body().jsonPath().getInt("numberOfItems")).isZero();
    assertThat(response.body().jsonPath().getBoolean("empty")).isTrue();
  }
}
