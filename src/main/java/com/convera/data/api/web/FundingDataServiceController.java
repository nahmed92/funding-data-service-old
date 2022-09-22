package com.convera.data.api.web;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.convera.common.template.CommonResponse;
import com.convera.common.template.response.error.constants.ResponseErrorCode404;
import com.convera.common.template.response.error.constants.ResponseErrorCode500;
import com.convera.common.template.response.util.CommonResponseUtil;
import com.convera.data.api.web.model.OrderPersistResponseModel;
import com.convera.data.api.web.model.OrderUpdateRequestModel;
import com.convera.data.repository.OrderRepository;
import com.convera.data.repository.model.Order;

import datadog.trace.api.Trace;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * The Funding Data Service provides the following:
 * <ul>
 * <li>1. Ability to persist order.</li>
 * <li>2. Ability to update order status and other attributes.</li>
 * <li>3. Ability to fetch order details (state of the order from funding
 * perspective).</li>
 *
 * </ul>
 */

@RestController
@RequestMapping("convera/funding")
@Slf4j
@Validated
@Tag(name = "Funding Data Service", description = "Funding Data Service")
public class FundingDataServiceController {
  @Autowired
  OrderRepository orderRepository;

  @Operation(operationId = "getOrder", responses = {
      @ApiResponse(responseCode = "200", description = "Get Order details", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderFetchResponse.class)) }),
      @ApiResponse(description = "Not found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)) }) })
  @Trace
  @GetMapping("orders/{orderId}")
  public ResponseEntity<?> getOrderDetails(
      @Parameter(description = "Order ID", example = "NTR3113812") @PathVariable String orderId,
      @RequestHeader(value = "correlationID", required = false) String correlationID) {

    final String orderPath = "/order/";
    Optional<Order> optionalOrder = orderRepository.findById(orderId);
    if (optionalOrder.isPresent()) {
      return ResponseEntity
          .ok(CommonResponseUtil.createResponse200(optionalOrder.get(), orderPath + orderId, correlationID));
    } else {
      CommonResponse<?> response404 = CommonResponseUtil.createResponse404(null, orderPath + orderId, correlationID,
          Collections.singletonList(ResponseErrorCode404.ERR_40400.build("funding", "Record for given id not found.")));
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response404);
    }

  }

  @Operation(operationId = "updateOrder", responses = {
      @ApiResponse(responseCode = "200", description = "Update Order details in persistence storage.", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderPersistenceResponse.class)) }),
      @ApiResponse(responseCode = "500", description = "unexpected error", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderPersistenceResponse.class)) }),
      @ApiResponse(responseCode = "404", description = "not found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)) }) })
  @Trace
  @PatchMapping(value = "orders/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommonResponse<OrderPersistResponseModel>> updateOrder(
      @Parameter(description = "Order ID", example = "NTR3113812") @PathVariable(required = true) String orderId,
      @RequestBody OrderUpdateRequestModel orderUpdateRequestModel,
      @RequestHeader(value = "correlationID", required = false) String correlationID) {

    OrderPersistResponseModel orderPersistResponseModel = null;
    try {
      Optional<Order> order = orderRepository.findById(orderId);

      if (order.isPresent()) {
        Order orderRec = order.get();

        orderRec.setStatus(orderUpdateRequestModel.getOrderStatus());
        orderRec.setFundedAmount(orderUpdateRequestModel.getFundedAmount());
        orderRec.setLastUpdatedOn(LocalDateTime.now(ZoneOffset.UTC));
        orderPersistResponseModel = getPersistResponseModel(order);
        orderRepository.save(orderRec);
      }

      if (orderPersistResponseModel != null) {
        return ResponseEntity
            .ok(CommonResponseUtil.createResponse200(orderPersistResponseModel, "funding/orders", correlationID));

      }

      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(CommonResponseUtil.createResponse404(null, "/order/" + orderId, correlationID, Collections
              .singletonList(ResponseErrorCode404.ERR_40400.build("funding", "Record for given id not found."))));

    } catch (Exception ex) {
      log.error("Error persisting order record: ", ex);
      return ResponseEntity.internalServerError()
          .body(CommonResponseUtil.createResponse500(orderPersistResponseModel, "funding/orders", correlationID,
              Collections.singletonList(ResponseErrorCode500.ERR_50000.build("funding-data-service",
                  "Error in updating the record in the DB. Message: " + ex.getMessage()))));

    }

  }

  @Operation(operationId = "persistOrder", responses = {
      @ApiResponse(responseCode = "200", description = "order response", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderPersistenceResponse.class)) }),
      @ApiResponse(description = "unexpected error", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderPersistenceResponse.class)) }) })
  @Trace
  @PostMapping(value = "orders", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommonResponse<OrderPersistResponseModel>> saveOrder(@RequestBody Order order,
      @RequestHeader(value = "correlationID", required = false) String correlationID) {
    try {

      Order dbOrder = orderRepository.save(order);
      OrderPersistResponseModel persistResponseModel = getPersistResponseModel(Optional.ofNullable(dbOrder));
      return ResponseEntity
          .ok(CommonResponseUtil.createResponse200(persistResponseModel, "funding/orders", correlationID));
    } catch (Exception ex) {
      log.error("Error persisting order record: ", ex);
      return ResponseEntity.internalServerError()
          .body(CommonResponseUtil.createResponse500(null, "funding/orders", correlationID,
              Collections.singletonList(ResponseErrorCode500.ERR_50000.build("funding-data-service",
                  "Error in saving the record in the DB. Message: " + ex.getMessage()))));

    }

  }

  private OrderPersistResponseModel getPersistResponseModel(Optional<Order> order) {
    OrderPersistResponseModel orderPersistResponseModel = null;
    if (order.isPresent()) {
      orderPersistResponseModel = OrderPersistResponseModel.builder().orderId(order.get().getOrderId())
          .status(order.get().getStatus()).lastUpdatedOn(order.get().getLastUpdatedOn()).build();
    }

    return orderPersistResponseModel;
  }

  private class OrderPersistenceResponse extends CommonResponse<OrderPersistResponseModel> {
  }

  private class OrderFetchResponse extends CommonResponse<Order> {
  }

}
