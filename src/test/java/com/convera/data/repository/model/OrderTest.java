package com.convera.data.repository.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class OrderTest {

  LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

  @Test
  void OrderAllArgumentConstructorTest() {
    Order order = new Order("NTR3113812", "COMMIT_ORDER","PENDING",now,now);
    assertThat(order.getOrderId()).isEqualTo("NTR3113812");
    assertThat(order.getOrderStatus()).isEqualTo("COMMIT_ORDER");
    assertThat(order.getFundingStatus()).isEqualTo("PENDING");

  }

  @Test
  void OrderNoArgumentConstructorTest() {
    Order order = new Order();
    assertThat(order).isNotNull();
  }

}
