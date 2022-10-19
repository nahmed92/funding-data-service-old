package com.convera.data.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @NotNull
  private String orderId;

  @NotNull
  private String orderStatus;

  @NotNull
 private String fundingStatus;

  @JsonProperty("createdOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime createdOn;

  @NotNull
  @JsonProperty("lastUpdatedOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime lastUpdatedOn;



}
