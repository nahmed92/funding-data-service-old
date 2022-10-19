package com.convera.data.api.web.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@ApiModel
@Data
@Builder
public class FundingUpdateRequestModel {
    private String orderId;
    private List<ContractFundingRequestModel> contractFundingList;
}
