package com.convera.data.api.web.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class OrderUpdateRequestModelTest {

  LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

  @Test
  void orderUpdateRequestModelAllArgumentConstructorTest() {
    OrderUpdateRequestModel order = new OrderUpdateRequestModel();
    order.setFundedAmount(new BigDecimal("3411"));
    order.setFundingStatus("Funded");
    assertThat(order.getFundedAmount()).isEqualTo(new BigDecimal("3411"));
    assertThat(order.getFundingStatus()).isEqualTo("Funded");
  }

  @Test
  void OrderNoArgumentConstructorTest() {
    OrderUpdateRequestModel order = new OrderUpdateRequestModel();
    assertThat(order).isNotNull();
  }

}
