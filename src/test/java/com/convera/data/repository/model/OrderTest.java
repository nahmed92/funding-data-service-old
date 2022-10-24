package com.convera.data.repository.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import org.junit.jupiter.api.Test;

class OrderTest {

  LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

  @Test
  void OrderAllArgumentConstructorTest() {
    Set<Contract> contract = Set.of(Contract.builder()
        .contractId("ContractId") //
        .orderId("order1") //
        .drawnDownAmount(new BigDecimal(100)) //
        .tradeAmount(new BigDecimal(10)) //
        .tradeCurrency("USD") //
        .build());
    
    Order order = Order.builder()
        .orderId("order1")
        .fundingStatus("PENDING") //
        .orderStatus("PENDING") //
        .contracts(contract) //
        .build();
    assertThat(order.getOrderId()).isEqualTo("order1");
    assertThat(order.getOrderStatus()).isEqualTo("PENDING");
    assertThat(order.getFundingStatus()).isEqualTo("PENDING");
    assertThat(order.getContracts().size()).isEqualTo(1);

  }

  @Test
  void OrderNoArgumentConstructorTest() {
    Order order = new Order();
    assertThat(order).isNotNull();
  }

}
