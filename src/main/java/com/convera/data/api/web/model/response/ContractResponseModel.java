package com.convera.data.api.web.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * contract Response Model.
 *
 * @Author: Sudarshan Datta
 */
@Data
@Builder
public class ContractResponseModel {

  @NotNull
  private String contractId;

  @NotNull
  private String orderId;

  private BigDecimal drawnDownAmount;

  private BigDecimal tradeAmount;

  private String tradeCurrency;

  @JsonProperty("createdOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime createdOn;

  @NotNull
  @JsonProperty("lastUpdatedOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime lastUpdatedOn;

  private List<ContractFundingResponseModel> contractFundings;
}
