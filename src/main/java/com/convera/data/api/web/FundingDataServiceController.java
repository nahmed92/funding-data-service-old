package com.convera.data.api.web;

import com.convera.common.template.CommonResponse;
import com.convera.common.template.response.error.constants.ResponseErrorCode404;
import com.convera.common.template.response.error.constants.ResponseErrorCode500;
import com.convera.common.template.response.util.CommonResponseUtil;
import com.convera.data.api.web.model.OrderPersistResponseModel;
import com.convera.data.api.web.model.request.FundingUpdateRequestModel;
import com.convera.data.api.web.model.request.OrderPersistRequestModel;
import com.convera.data.repository.model.Contract;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

/**
 * The Funding Data Service provides the following.
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
  public static final String FUNDING_SERVICE = "funding Data Service";

  @Autowired
  FundingService fundingService;

  /**
   * Get Order details .
   *
   * @param orderId       {@link String}
   * @param correlationid for tracing {@link String}
   * @return {@link ResponseEntity CommonResponse Order}
   */
  @Operation(operationId = "getOrder", responses = {
      @ApiResponse(responseCode = "200", description = "Get Order details", content = {
          @Content(mediaType = "application/json", schema = @Schema(
              implementation = OrderFetchResponse.class)) }),
      @ApiResponse(responseCode = "404", description = "Not found", content = {
          @Content(mediaType = "application/json", schema = @Schema(
              implementation = CommonResponse.class)) }) })
  @Trace
  @GetMapping("orders/{orderId}")
  public ResponseEntity<CommonResponse<Order>> getOrderDetails(
      @Parameter(description = "Order ID", example = "NTR3113812")
      @PathVariable String orderId,
      @RequestHeader(value = "correlationid", required = false) String correlationid) {

    final String orderPath = "/order/";
    Optional<Order> optionalOrder = fundingService.findOrderById(orderId);
    if (optionalOrder.isPresent()) {
      return ResponseEntity
          .ok(CommonResponseUtil.createResponse200(optionalOrder.get(),
              orderPath + orderId, correlationid));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(CommonResponseUtil.createResponse404(null,
              orderPath + orderId, correlationid, Collections
              .singletonList(ResponseErrorCode404.ERR_40400.build(
                  "funding", "Record for given id not found."))));
    }

  }

  /**
   * Get Contract Funding .
   *
   * @param orderId       {@link String}
   * @param contractId    {@link String}
   * @param correlationId for tracing {@link String}
   * @return {@link ResponseEntity CommonResponse List ContractFunding}
   */
  @Operation(operationId = "ContractOrderFunding", responses = {
      @ApiResponse(responseCode = "200", description = "Get Order details", content = {
          @Content(mediaType = "application/json", schema = @Schema(
              implementation = OrderFetchResponse.class)) }),
      @ApiResponse(responseCode = "404", description = "Not found", content = {
          @Content(mediaType = "application/json", schema = @Schema(
              implementation = CommonResponse.class)) }) })
  @Trace
  @GetMapping("/orders/{orderId}/contracts/{contractId}/contractsFunding/all")
  public ResponseEntity<CommonResponse<List<ContractFunding>>> getContractFunding(
      @Parameter(description = "Order ID", example = "NTR3113812")
      @PathVariable String orderId,
      @Parameter(description = "Contract ID", example = "Contract_1")
      @PathVariable String contractId,
      @RequestHeader(value = "correlationID", required = false) String correlationId) {

    final String orderPath = "/order/";
    try {
      List<ContractFunding> contractFunding = fundingService
          .getContractFundingByOrderIdAndContractId(orderId, contractId);
      return ResponseEntity
          .ok(CommonResponseUtil.createResponse200(contractFunding,
              orderPath + orderId, correlationId));
    } catch (HttpClientErrorException ex) {
      log.error("Order Not Found", ex);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(CommonResponseUtil.createResponse404(null,
              "/orders/" + orderId + "/contracts/" + contractId
                  + "/contractsFunding/all", correlationId, Collections.singletonList(
                  ResponseErrorCode404.ERR_40400.build(
                      "funding-service-api",
                      "Record for given id not found."))));
    } catch (Exception ex) {
      return ResponseEntity.internalServerError()
          .body(CommonResponseUtil.createResponse500(null,
              "/orders/" + orderId + "/contracts/" + contractId
                  + "contractsFunding/all", correlationId,
              Collections.singletonList(ResponseErrorCode500.ERR_50000.build(
                  "funding-service-api",
                  "Error in updating the record in the DB."
                    +  "Message:" + ex.getMessage()))));
    }

  }

  /**
   * Post Test insert in order .
   *
   * @param orderPersistRequestModel       {@link OrderPersistRequestModel}
   * @param correlationId for tracing {@link String}
   * @return {@link ResponseEntity CommonResponse Order}
   */
  @Operation(operationId = "createOrder", responses = {
      @ApiResponse(responseCode = "200", description = "Create Order with Contract.", content = {
          @Content(mediaType = "application/json", schema =
          @Schema(implementation = OrderFetchResponse.class)) }),
      @ApiResponse(responseCode = "500", description = "unexpected error", content = {
          @Content(mediaType = "application/json", schema =
          @Schema(implementation = OrderPersistenceResponse.class)) }),
      @ApiResponse(responseCode = "404", description = "not found", content = {
          @Content(mediaType = "application/json", schema =
          @Schema(implementation = CommonResponse.class)) }) })
  @Trace
  @PostMapping(value = "orders", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommonResponse<Order>> postTestInsert(
      @RequestBody OrderPersistRequestModel orderPersistRequestModel,
      @RequestHeader(value = "correlationId", required = false) String correlationId) {
    try {
      Order order = fundingService.createFundingOrderRecord(orderPersistRequestModel);
      return ResponseEntity.ok().body(CommonResponseUtil.createResponse200(order,
          "/convera/funding/orders", correlationId));
    } catch (Exception ex) {
      log.error("Exception while saving to database", ex);
      return ResponseEntity.internalServerError()
          .body(CommonResponseUtil.createResponse500(null,
              "/convera/funding/orders", correlationId, Collections.singletonList(
              ResponseErrorCode500.ERR_50000.build(FUNDING_SERVICE,
                  "Error While Saving Order" + ex.getMessage()))));
    }
  }

  /**
   * Create Contracts Finding.
   *
   * @param fundingUpdateRequestModel       {@link FundingUpdateRequestModel}
   * @param correlationId for tracing {@link String}
   * @return {@link ResponseEntity CommonResponse Set ContractFunding}
   */
  @Operation(operationId = "contractsFunding", responses = {
      @ApiResponse(responseCode = "200", description = "Create Order with Contract.", content = {
          @Content(mediaType = "application/json", schema =
          @Schema(implementation = OrderFetchResponse.class)) }),
      @ApiResponse(responseCode = "500", description = "unexpected error", content = {
          @Content(mediaType = "application/json", schema =
          @Schema(implementation = OrderPersistenceResponse.class)) }),
      @ApiResponse(responseCode = "404", description = "not found", content = {
          @Content(mediaType = "application/json", schema =
          @Schema(implementation = CommonResponse.class)) }) })
  @Trace
  @PostMapping("/contractsFunding")
  public ResponseEntity<CommonResponse<Set<ContractFunding>>> createContractsFunding(
      @RequestBody FundingUpdateRequestModel fundingUpdateRequestModel,
      @RequestHeader(value = "correlationId", required = false) String correlationId) {
    try {
      Set<ContractFunding> contractFundingSet = fundingService.insertContractFunding(
          fundingUpdateRequestModel);
      return ResponseEntity.ok()
          .body(CommonResponseUtil.createResponse200(contractFundingSet,
              "/contractsFunding/all", correlationId));
    } catch (Exception ex) {
      log.error("Exception while saving to database", ex);
      return ResponseEntity.internalServerError()
          .body(CommonResponseUtil.createResponse500(null,
              "/contractsFunding", correlationId,
              Collections.singletonList(ResponseErrorCode500.ERR_50000.build(
                  FUNDING_SERVICE, "Error While Saving Contract Funding"
                      + ex.getMessage()))));
    }
  }

  /**
   * Get Contract Order Funding .
   *
   * @param orderId       {@link String}
   * @param correlationId for tracing {@link String}
   * @return {@link ResponseEntity CommonResponse List Contract}
   */
  @Operation(operationId = "getConpleteOrder", responses = {
      @ApiResponse(responseCode = "200", description = "Create Order with Contract.", content = {
          @Content(mediaType = "application/json", schema =
          @Schema(implementation = OrderFetchResponse.class)) }),
      @ApiResponse(responseCode = "500", description = "unexpected error", content = {
          @Content(mediaType = "application/json", schema =
          @Schema(implementation = OrderPersistenceResponse.class)) }),
      @ApiResponse(responseCode = "404", description = "not found", content = {
          @Content(mediaType = "application/json", schema =
          @Schema(implementation = CommonResponse.class)) }) })
  @Trace
  @GetMapping("/orders/{orderId}/contracts/all")
  public ResponseEntity<CommonResponse<List<Contract>>> getContractOrderFunding(
      @PathVariable String orderId,
      @RequestHeader(value = "correlationId", required = false) String correlationId) {
    List<Contract> contracts = fundingService.getContractByOrderId(orderId);
    if (contracts != null && !contracts.isEmpty()) {
      return ResponseEntity
          .ok(CommonResponseUtil.createResponse200(contracts, "/orders/"
            + orderId + "/contracts/all", correlationId));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          CommonResponseUtil.createResponse404(null, "/orders/"
              + orderId + "/contracts/all", correlationId, Collections
              .singletonList(ResponseErrorCode404.ERR_40400.build(
                  "funding", "Record for given id not found."))));
    }
  }

  private class OrderPersistenceResponse extends CommonResponse<OrderPersistResponseModel> {
  }

  private class OrderFetchResponse extends CommonResponse<Order> {
  }

}
