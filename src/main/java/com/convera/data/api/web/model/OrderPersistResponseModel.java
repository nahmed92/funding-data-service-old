package com.convera.data.api.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime lastUpdatedOn;
}
