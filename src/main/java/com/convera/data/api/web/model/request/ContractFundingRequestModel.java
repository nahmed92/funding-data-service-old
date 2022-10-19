package com.convera.data.api.web.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
@Builder
public class ContractFundingRequestModel {
    @NotNull
    @NonNull
    private String contractId;

    @NotNull
    @NonNull
    private BigDecimal fundingAmount;

    private String fundingDate;

    @NotNull
    @NonNull
    private String fundingCurrency;

    private String bankRefNo;
}
