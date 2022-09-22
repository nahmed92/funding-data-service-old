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
    Order order = new Order("NTR3113812", "MP-CPL-1", "funded", "USD", now, now, new BigDecimal(1000),
        new BigDecimal(1000));
    assertThat(order.getOrderId()).isEqualTo("NTR3113812");
    assertThat(order.getCustomerId()).isEqualTo("MP-CPL-1");
    assertThat(order.getStatus()).isEqualTo("funded");
    assertThat(order.getCurrency()).isEqualTo("USD");
    assertThat(order.getFundedAmount()).isEqualTo(new BigDecimal(1000));
    assertThat(order.getTotalAmount()).isEqualTo(new BigDecimal(1000));
  }

  @Test
  void OrderNoArgumentConstructorTest() {
    Order order = new Order();
    assertThat(order).isNotNull();
  }

}
