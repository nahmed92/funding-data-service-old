package com.convera.data.api.web.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@Data
@Builder
public class ContractFundingRequestModel {
  @NotNull
  @NonNull
  @JsonProperty("contractId")
  private String contractId;

  @NotNull
  @NonNull
  @JsonProperty("fundingAmount")
  private BigDecimal fundingAmount;

  @JsonProperty("fundingDate")
  private String fundingDate;

  @NotNull
  @NonNull
  @JsonProperty("fundingCurrency")
  private String fundingCurrency;

  @JsonProperty("bankRefNo")
  private String bankRefNo;
}
