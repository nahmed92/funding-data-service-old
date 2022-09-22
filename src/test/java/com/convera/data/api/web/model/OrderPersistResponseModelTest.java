package com.convera.data.api.web.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class OrderPersistResponseModelTest {

  LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

  @Test
  void OrderPersistResponseModelAllArugTest() {
    OrderPersistResponseModel order = new OrderPersistResponseModel("NTR3113812", "Pending", now);
    assertThat(order.getOrderId()).isEqualTo("NTR3113812");
    assertThat(order.getStatus()).isEqualTo("Pending");
    assertThat(order.getLastUpdatedOn()).isEqualTo(now);
  }

  @Test
  void OrderPersistResponseModelNoArugTest() {
    OrderPersistResponseModel order = new OrderPersistResponseModel("NTR3113812", "Pending", now);
    assertThat(order).isNotNull();

  }
}
