package com.convera.data.api.web.model.request;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * contract Save Request Model.
 *
 * @Author: Sudarshan Datta
 */

@Data
@Builder
public class ContractSaveRequestModel {

  @NotNull
  @NonNull
  private String contractId;

  private BigDecimal drawnDownAmount;

  private BigDecimal tradeAmount;

  private String tradeCurrency;
}
