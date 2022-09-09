package com.convera.data.repository.model;

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
import java.math.BigDecimal;
import java.sql.Timestamp;

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
    private Timestamp createdOn;

   // @NotNull
    @JsonProperty("lastUpdatedOn")
    private Timestamp lastUpdatedOn;

   // @NotNull("lastUpdatedOn")
    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @NotNull
    @JsonProperty("fundedAmount")
    private BigDecimal fundedAmount;


}
