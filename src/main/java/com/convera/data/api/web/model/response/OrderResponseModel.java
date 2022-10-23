package com.convera.data.api.web.model.response;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class OrderResponseModel {

  private String orderId;

  @NotNull
  private String orderStatus;

  @NotNull
  private String fundingStatus;

  @NotNull
  private List<ContractResponseModel> contractResponseModels;
}
