package com.convera.data.api.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderUpdateRequestModel {
  @JsonProperty
  @NotNull
  @NonNull
  private String fundingStatus;

  @JsonProperty
  @NotNull
  @NonNull
  private BigDecimal fundedAmount;
}
