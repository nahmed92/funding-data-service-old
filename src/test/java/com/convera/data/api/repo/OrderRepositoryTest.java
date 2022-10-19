package com.convera.data.api.repo;

import com.convera.data.repository.OrderRepository;
import com.convera.data.repository.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@DataJpaTest
public class OrderRepositoryTest {

  @Autowired
  private TestEntityManager em;

  @Autowired
  private OrderRepository repository;

  LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

  @Test
  public void contextLoads() {
    Assertions.assertNotNull(em);
  }

  @Test
  void testOrderPersistsAndFetch() {
    Order orderPersisted = new Order("NTR3113812", "COMMIT_ORDER","PENDING", now, now);

    em.persist(orderPersisted);

    Optional<Order> order = repository.findById("NTR3113812");
    Assertions.assertEquals(Boolean.TRUE, order.isPresent());
    Assertions.assertEquals("NTR3113812", order.get().getOrderId());
    Assertions.assertEquals("PENDING", order.get().getFundingStatus());

  }

  @Test
  void testOrderUpdate() {
    Order orderPersisted = new Order("NTR3113812", "COMMIT_ORDER","PENDING", now, now);
    em.persist(orderPersisted);
    Optional<Order> order = repository.findById("NTR3113812");
    Assertions.assertEquals("PENDING", order.get().getFundingStatus());
    order.get().setOrderStatus("COMMIT");
    repository.save(order.get());
    order = null;
    order = repository.findById("NTR3113812");
    Assertions.assertEquals("COMMIT", order.get().getOrderStatus());
  }

}
