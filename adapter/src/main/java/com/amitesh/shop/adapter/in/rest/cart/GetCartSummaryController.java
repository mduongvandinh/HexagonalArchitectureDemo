package com.amitesh.shop.adapter.in.rest.cart;

import static com.amitesh.shop.adapter.in.rest.common.ControllerHelper.parseCustomerId;

import com.amitesh.shop.application.port.in.cart.GetCartSummaryUseCase;
import com.amitesh.shop.model.cart.CartSummary;
import com.amitesh.shop.model.customer.CustomerId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.CustomLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

@RestController
@RequestMapping("/carts")
@CustomLog
@ApiResponse(
    responseCode = "500",
    description = "Internal server error, this should not happen",
    content =
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = InternalServerError.class)))
@Tag(name = "Cart Summary Controller", description = "Endpoint for getting Cart summary")
public class GetCartSummaryController {

  private final GetCartSummaryUseCase getCartSummaryUseCase;

  public GetCartSummaryController(GetCartSummaryUseCase getCartSummaryUseCase) {
    this.getCartSummaryUseCase = getCartSummaryUseCase;
  }

  @GetMapping("/{customerId}/summary")
  @Operation(
      operationId = "GetCartSummary",
      summary = "Get a lightweight summary of the cart",
      responses =
          @ApiResponse(
              responseCode = "200",
              description = "Cart summary is returned",
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = CartSummaryWebModel.class))))
  public CartSummaryWebModel getCartSummary(
      @PathVariable("customerId") String customerIdString) {

    LOGGER.debug("Fetching cart summary for {}", customerIdString);

    CustomerId customerId = parseCustomerId(customerIdString);
    CartSummary summary = getCartSummaryUseCase.getCartSummary(customerId);

    return CartSummaryWebModel.fromDomainModel(summary);
  }
}
