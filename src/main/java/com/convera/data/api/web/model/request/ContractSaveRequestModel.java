package com.convera.data.api.web.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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
