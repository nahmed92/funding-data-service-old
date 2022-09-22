package com.convera.data.api.web.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

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
  @JsonProperty("lastUpdatedOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime lastUpdatedOn;
}
