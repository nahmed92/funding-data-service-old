package com.convera.data.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @NotNull
    private String contractId;

    @NotNull
    private String orderId;

   private BigDecimal drawnDownAmount;

    private BigDecimal tradeAmount;

    private String tradeCurrency;

    @JsonProperty("createdOn")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdOn;

    @NotNull
    @JsonProperty("lastUpdatedOn")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime lastUpdatedOn;

  /*  @ManyToOne
    @JoinColumn(name="orderId", nullable=false,insertable = false,updatable = false)
    @JsonIgnore
    private Order order;*/

   /* @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<ContractFunding> contracts;*/

}
