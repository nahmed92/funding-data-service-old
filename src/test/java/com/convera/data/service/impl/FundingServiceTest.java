package com.convera.data.service.impl;

import com.convera.data.api.web.model.request.ContractFundingRequestModel;
import com.convera.data.api.web.model.request.FundingUpdateRequestModel;
import com.convera.data.api.web.model.request.OrderPersistRequestModel;
import com.convera.data.repository.ContractFundingRepository;
import com.convera.data.repository.ContractRepository;
import com.convera.data.repository.OrderRepository;
import com.convera.data.repository.model.Contract;
import com.convera.data.repository.model.ContractFunding;
import com.convera.data.repository.model.Order;
import com.convera.data.service.FundingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class FundingServiceTest {

  @MockBean
  private ContractFundingRepository contractFundingRepository;

  @MockBean
  private ContractRepository contractRepository;

  @MockBean
  private OrderRepository orderRepository;

  @Autowired
  private FundingService fundingService;

  LocalDateTime now;
  SimpleDateFormat format;
  Date date2;

  @BeforeEach
  void init(){
    now = LocalDateTime.now(ZoneOffset.UTC);
    format = new SimpleDateFormat("dd-MM-yy");
    date2 = new Date(20232022L);
  }

  @Test
  void createFundingOrderRecordTest_shouldCreateFundingOrderRecord() {
    Set<Contract> contracts = getContracts();
    Order order = getOrder(contracts);

    OrderPersistRequestModel orderPersist = OrderPersistRequestModel.builder()
        .orderId(order.getOrderId())
        .contractList(new ArrayList<>())
        .orderStatus(order.getOrderStatus())
        .fundingStatus(order.getFundingStatus())
        .build();

    when(orderRepository.save(any(Order.class))).thenReturn(order);
    Order responseOrder = fundingService.createFundingOrderRecord(orderPersist);

    verify(orderRepository, times(1)).save(any(Order.class));
    assertEquals(order, responseOrder);
  }

  @Test
  void insertContractFundingTest_shouldSaveAllContractFundingSet(){
    List<ContractFundingRequestModel> contractFundings = new ArrayList<>();
    ContractFundingRequestModel contractFundingRequestModel = ContractFundingRequestModel.builder()
        .fundingAmount(new BigDecimal(20))
        .fundingCurrency("USD")
        .contractId("contract_id")
        .fundingDate("01-12-2018")
        .bankRefNo("bank_ref")
        .build();
    contractFundings.add(contractFundingRequestModel);

    FundingUpdateRequestModel fundingUpdateRequestModel = FundingUpdateRequestModel.builder()
        .orderId("orderId")
        .contractFundings(contractFundings)
        .build();

    doReturn(new ArrayList<>()).when(contractFundingRepository).saveAll(any());
    Set<ContractFunding> respondedContractFunding = fundingService.insertContractFunding(fundingUpdateRequestModel);

    verify(contractFundingRepository, times(1)).saveAll(any());
    assertEquals(contractFundings.size(), respondedContractFunding.size());
  }

  @Test
  void getContractFundingByOrderIdAndContractIdTest_shouldReturnContractFundingList(){
    List<Contract> contracts = getContracts().stream().toList();
    List<ContractFunding> contractFundings = new ArrayList<>();
    contractFundings.add(new ContractFunding());

    when(contractRepository.findByOrderIdAndContractId(anyString(), anyString())).thenReturn(contracts);
    when(contractFundingRepository.findByContractId(anyString())).thenReturn(contractFundings);

    fundingService.getContractFundingByOrderIdAndContractId("orderId", "contractId");

    verify(contractRepository, times(1)).findByOrderIdAndContractId(anyString(), anyString());
    verify(contractFundingRepository, times(1)).findByContractId(anyString());
  }

  @Test
  void getContractFundingByOrderIdAndContractIdTest_shouldThrowHttpClientErrorException(){
    List<Contract> contracts = new ArrayList<>();

    when(contractRepository.findByOrderIdAndContractId(anyString(), anyString())).thenReturn(contracts);

    assertThrows(HttpClientErrorException.class, () -> {
      fundingService.getContractFundingByOrderIdAndContractId("orderId", "contractId");
    });
  }

  @Test
  void getContractFundingTest_ShouldReturnContractFundingList() {
    when(contractFundingRepository.findByContractId(anyString())).thenReturn(new ArrayList<>());
    fundingService.getContractFunding("contract_1");
    verify(contractFundingRepository, times(1)).findByContractId(anyString());
  }

  @Test
  void getContractByOrderIdTest_shouldReturnContractList() {
    when(contractRepository.findByOrderId(anyString())).thenReturn(new ArrayList<>());
    fundingService.getContractByOrderId("order_1");
    verify(contractRepository, times(1)).findByOrderId(anyString());
  }

  @Test
  void findOrderByIdTest_shouldReturnOrder() {
    when(orderRepository.findById(anyString())).thenReturn(Optional.of(new Order()));
    fundingService.findOrderById("order_1");
    verify(orderRepository, times(1)).findById(anyString());
  }
  private Set<Contract> getContracts(){
    Set<Contract> contracts = new HashSet<>();
    Contract contract = Contract.builder() //
        .contractId("contract_1") //
        .orderId("order_1") //
        .drawnDownAmount(new BigDecimal(100)) //
        .tradeAmount(new BigDecimal(30)) //
        .tradeCurrency("USD") //
        .createdOn(now) //
        .lastUpdatedOn(now) //
        .build();
    contracts.add(contract);
    return contracts;
  }

  private Order getOrder(Set<Contract> contracts){
    return Order.builder() //
        .orderId("order_1") //
        .orderStatus("pending") //
        .fundingStatus("pending") //
        .contracts(contracts) //
        .createdOn(now) //
        .lastUpdatedOn(now) //
        .build();
  }
}

