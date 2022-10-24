package com.convera.data.api.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderSaveRequestModel {

  @NotNull
  @NonNull
  @JsonProperty("orderId")
  private String orderId;

  @NotNull
  @NonNull
  @JsonProperty("customerId")
  private String customerId;

  @NotNull
  @NonNull
  @JsonProperty("status")
  private String status;

  @NotNull
  @NonNull
  @JsonProperty("currency")
  private String currency;

  @NotNull
  @NonNull
  @JsonProperty("totalAmount")
  private BigDecimal totalAmount;

  @JsonProperty("fundedAmount")
  private BigDecimal fundedAmount;

  @JsonProperty("createdOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime createdOn;

  @NotNull
  @NonNull
  @JsonProperty("lastUpdatedOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime lastUpdatedOn;

  @JsonProperty("fundingStatus")
  private String fundingStatus;
}
