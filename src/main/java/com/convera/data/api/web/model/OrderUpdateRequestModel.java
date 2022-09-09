package com.convera.data.api.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderUpdateRequestModel {
    @JsonProperty
    private String orderStatus;

    @JsonProperty
    private BigDecimal fundedAmount;
}
