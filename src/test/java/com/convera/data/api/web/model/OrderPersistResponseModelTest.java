package com.convera.data.api.web.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class OrderPersistResponseModelTest {

  LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

  @Test
  void OrderPersistResponseModelAllArgTest() {
    OrderPersistResponseModel order = new OrderPersistResponseModel("NTR3113812", "COMMIT", "PENDING",now);
    assertThat(order.getOrderId()).isEqualTo("NTR3113812");
    assertThat(order.getStatus()).isEqualTo("COMMIT");
    assertThat(order.getFundingStatus()).isEqualTo("PENDING");
    assertThat(order.getLastUpdatedOn()).isEqualTo(now);
  }

  @Test
  void OrderPersistResponseModelNoArgTest() {
    OrderPersistResponseModel order = new OrderPersistResponseModel("NTR3113812", "COMMIT", "PENDING", now);
    assertThat(order).isNotNull();

  }
}
