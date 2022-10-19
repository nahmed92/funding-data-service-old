package com.convera.data.api.web;

import com.convera.common.template.CommonResponse;
import com.convera.common.template.response.error.constants.ResponseErrorCode404;
import com.convera.common.template.response.error.constants.ResponseErrorCode500;
import com.convera.common.template.response.util.CommonResponseUtil;
import com.convera.data.api.web.model.OrderPersistResponseModel;
import com.convera.data.api.web.model.OrderUpdateRequestModel;
import com.convera.data.api.web.model.request.FundingUpdateRequestModel;
import com.convera.data.api.web.model.request.OrderPersistRequestModel;
import com.convera.data.api.web.model.response.OrderResponseModel;
import com.convera.data.repository.OrderRepository;
import com.convera.data.repository.model.ContractFunding;
import com.convera.data.repository.model.Order;
import com.convera.data.service.FundingService;
import datadog.trace.api.Trace;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

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
  public static final String FUNDING_ORDERS = "funding/orders";
  @Autowired
  OrderRepository orderRepository;

  @Autowired
  FundingService fundingService;


  @Operation(operationId = "getOrder", responses = {
      @ApiResponse(responseCode = "200", description = "Get Order details", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderFetchResponse.class)) }),
      @ApiResponse(responseCode = "404", description = "Not found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)) }) })
  @Trace
  @GetMapping("orders/{orderId}")
  public ResponseEntity<CommonResponse<Order>> getOrderDetails(
      @Parameter(description = "Order ID", example = "NTR3113812") @PathVariable String orderId,
      @RequestHeader(value = "correlationID", required = false) String correlationID) {

    final String orderPath = "/order/";
    Optional<Order> optionalOrder = orderRepository.findById(orderId);
    if (optionalOrder.isPresent()) {
      return ResponseEntity
          .ok(CommonResponseUtil.createResponse200(optionalOrder.get(), orderPath + orderId, correlationID));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonResponseUtil.createResponse404(null, orderPath + orderId, correlationID,
              Collections.singletonList(ResponseErrorCode404.ERR_40400.build("funding", "Record for given id not found."))));
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

        orderRec.setFundingStatus(orderUpdateRequestModel.getFundingStatus());
      //  orderRec.setFundedAmount(orderUpdateRequestModel.getFundedAmount());
        orderRec.setLastUpdatedOn(LocalDateTime.now(ZoneOffset.UTC));
       // orderPersistResponseModel = getPersistResponseModel(order);
        orderRepository.save(orderRec);
      }

      if (orderPersistResponseModel != null) {
        return ResponseEntity
            .ok(CommonResponseUtil.createResponse200(orderPersistResponseModel, FUNDING_ORDERS, correlationID));

      }

      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(CommonResponseUtil.createResponse404(null, "/order/" + orderId, correlationID, Collections
              .singletonList(ResponseErrorCode404.ERR_40400.build("funding", "Record for given id not found."))));

    } catch (Exception ex) {
      log.error("Error persisting order record: ", ex);
      return ResponseEntity.internalServerError()
          .body(CommonResponseUtil.createResponse500(orderPersistResponseModel, FUNDING_ORDERS, correlationID,
              Collections.singletonList(ResponseErrorCode500.ERR_50000.build("funding-data-service",
                  "Error in updating the record in the DB. Message: " + ex.getMessage()))));
    }

  }

  @PostMapping("test")
  public ResponseEntity<CommonResponse<Order>> postTestInsert(@RequestBody OrderPersistRequestModel orderPersistRequestModel)
  {

   Order order =fundingService.insertFundingRecord(orderPersistRequestModel);
   return ResponseEntity.ok().body(CommonResponseUtil.createResponse200(order,"test",null,null));

  }

  @PostMapping("testUpdate")
  public ResponseEntity<CommonResponse<Set<ContractFunding>>> postTestUpdate(@RequestBody FundingUpdateRequestModel fundingUpdateRequestModel)
  {
    Set<ContractFunding> contractFundingSet = fundingService.insertContractFunding(fundingUpdateRequestModel);
    return ResponseEntity.ok().body(CommonResponseUtil.createResponse200(contractFundingSet,"testUpdate",null,null));

  }


  @GetMapping("/testGetOrder/{contractId}")
  public OrderResponseModel getContractOrderFunding(@PathVariable String contractId)
  {
    OrderResponseModel order = fundingService.getCompleteOrderByContractId(contractId);
    return order;
  }

  /*@Operation(operationId = "persistOrder", responses = {
      @ApiResponse(responseCode = "200", description = "order response", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderPersistenceResponse.class)) }),
      @ApiResponse(responseCode = "500", description = "unexpected error", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)) }) })
  @Trace
  @PostMapping(value = "orders", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommonResponse<OrderPersistResponseModel>> saveOrder(@RequestBody OrderSaveRequestModel orderSaveModel,
      @RequestHeader(value = "correlationID", required = false) String correlationID) {
    try {

      Order orderToSave = getOrderToSave(orderSaveModel);
      Order dbOrder = orderRepository.save(orderToSave);
      OrderPersistResponseModel persistResponseModel = getPersistResponseModel(Optional.ofNullable(dbOrder));
      return ResponseEntity
          .ok(CommonResponseUtil.createResponse200(persistResponseModel, FUNDING_ORDERS, correlationID));
    } catch (Exception ex) {
      log.error("Error persisting order record: ", ex);
      return ResponseEntity.internalServerError()
          .body(CommonResponseUtil.createResponse500(null, FUNDING_ORDERS, correlationID,
              Collections.singletonList(ResponseErrorCode500.ERR_50000.build("funding-data-service",
                  "Error in saving the record in the DB. Message: " + ex.getMessage()))));

    }

  }*/

  /*private Order getOrderToSave(OrderSaveRequestModel orderSaveModel) {
    return new Order(orderSaveModel.getOrderId(),
            orderSaveModel.getCustomerId(),
            orderSaveModel.getStatus(),
            orderSaveModel.getCurrency(),
            orderSaveModel.getTotalAmount(),
            orderSaveModel.getFundedAmount(),
            orderSaveModel.getCreatedOn(),
            orderSaveModel.getLastUpdatedOn(),
            orderSaveModel.getFundingStatus());


  }*/

/*  private OrderPersistResponseModel getPersistResponseModel(Optional<Order> order) {
    OrderPersistResponseModel orderPersistResponseModel = null;
    if (order.isPresent()) {
      orderPersistResponseModel = OrderPersistResponseModel.builder().orderId(order.get().getOrderId())
          .status(order.get().getStatus()).lastUpdatedOn(order.get().getLastUpdatedOn())
              .fundingStatus(order.get().getFundingStatus()).build();
    }

    return orderPersistResponseModel;
  }*/

  private class OrderPersistenceResponse extends CommonResponse<OrderPersistResponseModel> {
  }

  private class OrderFetchResponse extends CommonResponse<Order> {
  }

}
