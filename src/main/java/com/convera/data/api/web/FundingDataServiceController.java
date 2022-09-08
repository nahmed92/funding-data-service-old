package com.convera.data.api.web;

import com.convera.common.template.CommonResponse;
import com.convera.common.template.response.error.constants.ResponseErrorCode404;
import com.convera.common.template.response.error.constants.ResponseErrorCode500;
import com.convera.common.template.response.util.CommonResponseUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("convera/funding")
@Slf4j
@Validated
@Tag(name = "Funding Data Service", description = "Funding Data Service")
public class FundingDataServiceController {
    @Autowired
    OrderRepository orderRepository;


    @Operation(
            operationId = "getOrder",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get Order details", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))
                    }),
                    @ApiResponse(description = "Not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))
                    })
            }
    )
    @Trace
    @GetMapping("orders/{id}")
    public ResponseEntity<?> getOrderDetails(@Parameter(description = "Order ID", example = "NTR3113812") @PathVariable String id,@RequestHeader(value = "correlationID", required = false) String correlationID)  {

        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            return ResponseEntity.ok(CommonResponseUtil.createResponse200(
                    optionalOrder.get(),
                    "/order/"+id,
                    correlationID));
        }else{
            CommonResponse<?> response404 = CommonResponseUtil.createResponse404(null, "/order/" + id,
                    correlationID, Collections.singletonList(ResponseErrorCode404.ERR_40400.build("funding", "Record for given id not found.")));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response404);
        }

       // throw new DataNotFoundException("Record not found with ID: " + id);

    }

    @Operation(
            operationId = "saveOrder",
            responses = {
                    @ApiResponse(responseCode = "200", description = "product response", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))
                    }),
                    @ApiResponse(description = "unexpected error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))
                    })
            }
    )
    @Trace
    @PostMapping(value = "orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> saveProduct(@RequestBody Order order,@RequestHeader(value = "correlationID", required = false) String correlationID) {
        try {

            Order dbOrder = orderRepository.save(order);
            return ResponseEntity.ok(CommonResponseUtil.createResponse200(dbOrder.getOrderId(),"funding/orders",correlationID));
        }catch (Exception ex)
        {
           log.error("Error persisting order record: ",ex);
            return ResponseEntity.internalServerError().body(CommonResponseUtil.createResponse500(null,
                    "funding/orders",correlationID,
                    Collections.singletonList(ResponseErrorCode500.ERR_50000
                            .build("funding-data-service","Error is saving the record in the DB. Message: "+ex.getMessage()))));

        }


    }

}
