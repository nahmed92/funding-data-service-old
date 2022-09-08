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
    @JsonProperty
    private String orderId;

    @NotNull
    @JsonProperty
    private String customerId;

    @NotNull
    @JsonProperty
    private String status;

    @NotNull
    @JsonProperty
    private String currency;

    @NotNull
    @JsonProperty
    private Timestamp createdOn;

   // @NotNull
    @JsonProperty
    private Timestamp lastUpdatedOn;

   // @NotNull
    @JsonProperty
    private BigDecimal totalAmount;

    @NotNull
    @JsonProperty
    private BigDecimal fundedAmount;


}
