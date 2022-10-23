package com.convera.data.api.repo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.convera.data.repository.ContractRepository;
import com.convera.data.repository.model.Contract;

@DataJpaTest
public class ContractReposotoryTest {

  @Autowired
  private TestEntityManager em;
  
  @Autowired
  private ContractRepository repository;
  

  LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

  
  //@Test
  public void contextLoads() {
    Assertions.assertNotNull(em);
  }
  
  //@Test
  void shouldFetchContract() {
    Contract contract  = Contract.builder()
        .contractId("ContractId") //
        .orderId("order1") //
        .drawnDownAmount(new BigDecimal(100)) //
        .tradeAmount(new BigDecimal(10)) //
        .tradeCurrency("USD") //
        .createdOn(now) //
        .lastUpdatedOn(now) //
        .build();
    repository.save(contract);
    List<Contract> contracts = repository.findByOrderId("order1");
    Assertions.assertEquals("1", contracts.size());
    Assertions.assertEquals("contractId", contracts.get(0).getContractId());
    Assertions.assertEquals(new BigDecimal(100), contracts.get(0).getDrawnDownAmount());
  }
}
