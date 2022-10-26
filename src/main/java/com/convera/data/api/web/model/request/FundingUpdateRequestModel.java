package com.convera.data.api.web.model.request;

import io.swagger.annotations.ApiModel;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Funding Update Request Model.
 *
 * @Author: Sudarshan Datta
 */
@ApiModel
@Data
@Builder
public class FundingUpdateRequestModel {
  private String orderId;
  private List<ContractFundingRequestModel> contractFundings;
}
