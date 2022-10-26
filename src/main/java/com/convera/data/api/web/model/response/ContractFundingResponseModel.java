package com.convera.data.api.web.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * contract Funding Response Model.
 *
 * @Author: Sudarshan Datta
 */
@Data
@Builder
public class ContractFundingResponseModel {

  @NotNull
  private String uuid;

  @NotNull
  private String contractId;
  private BigDecimal fundingAmount;
  private String fundingDate;
  private String fundingCurrency;
  private String bankRefNo;

  @JsonProperty("createdOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime createdOn;

  @NotNull
  @JsonProperty("lastUpdatedOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime lastUpdatedOn;
}
