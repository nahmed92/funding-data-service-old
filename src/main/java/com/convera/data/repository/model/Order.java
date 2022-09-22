package com.convera.data.repository.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @JsonProperty("orderId")
  private String orderId;

  @NotNull
  @JsonProperty("customerId")
  private String customerId;

  @NotNull
  @JsonProperty("status")
  private String status;

  @NotNull
  @JsonProperty("currency")
  private String currency;

  @NotNull
  @JsonProperty("createdOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime createdOn;

  @JsonProperty("lastUpdatedOn")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime lastUpdatedOn;

  @JsonProperty("totalAmount")
  private BigDecimal totalAmount;

  @NotNull
  @JsonProperty("fundedAmount")
  private BigDecimal fundedAmount;

}
