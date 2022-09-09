package com.convera.data.api.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Builder
public class OrderPersistResponseModel {
    @NotNull
    @JsonProperty
    private String orderId;

    @NotNull
    @JsonProperty
    private String status;

    @NotNull
    @JsonProperty
    private Timestamp lastUpdatedOn;
}
