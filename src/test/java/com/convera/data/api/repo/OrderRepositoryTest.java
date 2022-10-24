package com.convera.data.api.repo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.convera.data.repository.OrderRepository;
import com.convera.data.repository.model.Contract;
import com.convera.data.repository.model.Order;

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
    Set<Contract> contract = Set.of(Contract.builder()
        .contractId("ContractId") //
        .orderId("order1") //
        .drawnDownAmount(new BigDecimal(100)) //
        .tradeAmount(new BigDecimal(10)) //
        .tradeCurrency("USD") //
        .build());
    
    Order orderPersisted = Order.builder()
        .orderId("order1")
        .fundingStatus("PENDING") //
        .orderStatus("PENDING") //
        .contracts(contract) //
        .build();

    em.persist(orderPersisted);

    Optional<Order> order = repository.findById("order1");
    Assertions.assertEquals(Boolean.TRUE, order.isPresent());
    Assertions.assertEquals("order1", order.get().getOrderId());
    Assertions.assertEquals("PENDING", order.get().getFundingStatus());
    Assertions.assertEquals(1, order.get().getContracts().size());

  }

  @Test
  void testOrderUpdate() {
    Set<Contract> contracts = new HashSet<>();
        
    contracts.add(Contract.builder()
        .contractId("ContractId") //
        .orderId("order1") //
        .drawnDownAmount(new BigDecimal(100)) //
        .tradeAmount(new BigDecimal(10)) //
        .tradeCurrency("USD") //
        .build());
    
    Order orderPersisted = Order.builder()
        .orderId("order1")
        .fundingStatus("PENDING") //
        .orderStatus("PENDING") //
        .contracts(contracts) //
        .build();

    em.persist(orderPersisted);
    
    contracts.add(Contract.builder()
        .contractId("ContractId_2") //
        .orderId("order1") //
        .drawnDownAmount(new BigDecimal(200)) //
        .tradeAmount(new BigDecimal(20)) //
        .tradeCurrency("UAD") //
        .build());
    Order updateOrderPersisted = Order.builder()
        .orderId("order1")
        .fundingStatus("FUNDED") //
        .orderStatus("COMMIT") //
        .contracts(contracts)
        .build();
    
    
    repository.save(updateOrderPersisted);
    Optional<Order> order = repository.findById("order1");
    Assertions.assertEquals("COMMIT", order.get().getOrderStatus());
    Assertions.assertEquals(2 , order.get().getContracts().size());
  }

}
