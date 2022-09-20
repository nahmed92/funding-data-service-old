package com.convera;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.convera.data.api.web.FundingDataServiceController;
import com.convera.data.repository.OrderRepository;

@SpringBootTest
class FundingDataServiceApplicationTest {

  @Autowired
  private FundingDataServiceController controller;
  
  @Autowired
  private OrderRepository repository;
  
  @Mock
  private FundingDataServiceApplication fundingDataServiceApplication;
  
  
  @Test
  void contextLoads() {
    Assertions.assertNotNull(controller);
    Assertions.assertNotNull(repository);
  }

  @Test
  void shouldTestMain() {
    fundingDataServiceApplication.main(new String[] {});
  }
}
