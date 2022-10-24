package com.convera.data.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.convera.data.api.web.model.request.ContractFundingRequestModel;
import com.convera.data.api.web.model.request.ContractSaveRequestModel;
import com.convera.data.api.web.model.request.FundingUpdateRequestModel;
import com.convera.data.api.web.model.request.OrderPersistRequestModel;
import com.convera.data.repository.ContractFundingRepository;
import com.convera.data.repository.ContractRepository;
import com.convera.data.repository.OrderRepository;
import com.convera.data.repository.model.Contract;
import com.convera.data.repository.model.ContractFunding;
import com.convera.data.repository.model.Order;
import com.convera.data.service.FundingService;

@Service("FundingService")
public class FundingServiceImpl implements FundingService {
  @Autowired
  OrderRepository orderRepository;

  @Autowired
  ContractRepository contractRepository;

  @Autowired
  ContractFundingRepository contractFundingRepository;

  public Order createFundingOrderRecord(OrderPersistRequestModel orderPersistRequestModel) {
    Order order = Order.builder() //
        .orderId(orderPersistRequestModel.getOrderId()) //
        .orderStatus(orderPersistRequestModel.getOrderStatus()) //
        .fundingStatus(orderPersistRequestModel.getFundingStatus()) //
        .contracts(createContractsFromRequest(orderPersistRequestModel.getOrderId(),
            orderPersistRequestModel.getContractList())) //
        .createdOn(LocalDateTime.now(ZoneOffset.UTC)) //
        .lastUpdatedOn(LocalDateTime.now(ZoneOffset.UTC)) //
        .build();

    return orderRepository.save(order);
  }

  private Set<Contract> createContractsFromRequest(String orderId,
      List<ContractSaveRequestModel> contractRequestModels) {
    Set<Contract> contracts = new HashSet<>();
    contractRequestModels.stream().forEach(contratRequest -> {
      Contract contract = Contract.builder() //
          .contractId(contratRequest.getContractId()) //
          .orderId(orderId) //
          .drawnDownAmount(contratRequest.getDrawnDownAmount()) //
          .tradeAmount(contratRequest.getTradeAmount()) //
          .tradeCurrency(contratRequest.getTradeCurrency()) //
          .createdOn(LocalDateTime.now(ZoneOffset.UTC)) //
          .lastUpdatedOn(LocalDateTime.now(ZoneOffset.UTC)) //
          .build();
      contracts.add(contract);
    });

    return contracts;
  }

  public Set<ContractFunding> insertContractFunding(FundingUpdateRequestModel fundingUpdateRequestModel) {
    Set<ContractFunding> contractFundingSet = new HashSet<>();

    for (ContractFundingRequestModel contractFundingRequestModel : fundingUpdateRequestModel.getContractFundings()) {
      ContractFunding conFund = new ContractFunding(UUID.randomUUID().toString(),
          contractFundingRequestModel.getContractId(), contractFundingRequestModel.getFundingAmount(),
          contractFundingRequestModel.getFundingDate(), contractFundingRequestModel.getFundingCurrency(),
          contractFundingRequestModel.getBankRefNo(), LocalDateTime.now(), LocalDateTime.now());
      contractFundingSet.add(conFund);
    }
    contractFundingRepository.saveAll(contractFundingSet);
    return contractFundingSet;

  }

  @Override
  public List<ContractFunding> getContractFundingByOrderIdAndContractId(String orderId, String contractId) {
    List<Contract> contracts = contractRepository.findByOrderIdAndContractId(orderId, contractId);
    if (contracts.isEmpty()) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
    return contractFundingRepository.findByContractId(contractId);
  }

  @Override
  public List<ContractFunding> getContractFunding(String contractId) {
    return contractFundingRepository.findByContractId(contractId);
  }

  @Override
  public List<Contract> getContractByOrderId(String orderId) {
    return contractRepository.findByOrderId(orderId);
  }

  @Override
  public Optional<Order> findOrderById(String orderId) {
    return orderRepository.findById(orderId);
  }

//  public OrderResponseModel getCompleteOrderByContractId(String contractId) {
//
//    // Get all contracts based on orderId
//    Optional<Contract> contract = contractRepository.findById(contractId);
//
//    if (contract.isEmpty()) {
//      throw new DataNotFoundException("Data not found in contract table: " + contractId);
//    }
//    List<Contract> allContracts = contractRepository.findByOrderId(contract.get().getOrderId());
//
//    // Get the order
//    Optional<Order> order = orderRepository.findById(contract.get().getOrderId());
//
//    // TODO: Represent all in single web response object
//    OrderResponseModel orderResponseModel = transformToOrderResponseModel(order.get(), allContracts);
//    return orderResponseModel;
//
//  }
//
//  private OrderResponseModel transformToOrderResponseModel(Order order, List<Contract> allContracts) {
//    return OrderResponseModel.builder().orderId(order.getOrderId()) //
//        .orderStatus(order.getOrderStatus()) //
//        .fundingStatus(order.getFundingStatus()) //
//        .contractResponseModels(transformToContractResponseModel(allContracts)) //
//        .build();
//  }
//
//  private List<ContractResponseModel> transformToContractResponseModel(List<Contract> allContracts) {
//    List<ContractResponseModel> contractResponseModels = new ArrayList<>();
//    for (Contract contract : allContracts) {
//      List<ContractFunding> contractFundingList = contractFundingRepository.findByContractId(contract.getContractId());
//      ContractResponseModel contractResponseModel = ContractResponseModel.builder().contractId(contract.getContractId())
//          .contractFundings(transformToContractFundingResponseModel(contractFundingList))
//          .drawnDownAmount(contract.getDrawnDownAmount()).tradeAmount(contract.getTradeAmount())
//          .tradeCurrency(contract.getTradeCurrency()).contractId(contract.getContractId())
//          .orderId(contract.getOrderId()).createdOn(contract.getCreatedOn()).lastUpdatedOn(contract.getLastUpdatedOn())
//          .build();
//
//      contractResponseModels.add(contractResponseModel);
//
//    }
//
//    return contractResponseModels;
//  }
//
//  private List<ContractFundingResponseModel> transformToContractFundingResponseModel(
//      List<ContractFunding> contractFundingList) {
//    List<ContractFundingResponseModel> contractFundingResponseModelList = new ArrayList<>();
//    for (ContractFunding contractFunding : contractFundingList) {
//      ContractFundingResponseModel contractFundingResponseModel = ContractFundingResponseModel.builder()
//          .bankRefNo(contractFunding.getBankRefNo()).contractId(contractFunding.getContractId())
//          .fundingAmount(contractFunding.getFundingAmount()).fundingCurrency(contractFunding.getFundingCurrency())
//          .fundingDate(contractFunding.getFundingDate()).uuid(contractFunding.getUuid())
//          .createdOn(contractFunding.getCreatedOn()).lastUpdatedOn(contractFunding.getLastUpdatedOn()).build();
//      contractFundingResponseModelList.add(contractFundingResponseModel);
//    }
//
//    return contractFundingResponseModelList;
//
//  }
//
//  private Set<Contract> getContractSet(List<ContractSaveRequestModel> contractSaveRequestModels, String orderId) {
//    Set<Contract> contracts = new LinkedHashSet<>();
//
//    if (CollectionUtils.isEmpty(contractSaveRequestModels)) {
//      return contracts;
//    }
//    contractSaveRequestModels.stream().forEach(contract -> {
//      contracts.add(Contract.builder() //
//          .contractId(contract.getContractId()) //
//          .orderId(orderId) //
//          .drawnDownAmount(contract.getDrawnDownAmount()) //
//          .tradeAmount(contract.getTradeAmount()) //
//          .tradeCurrency(contract.getTradeCurrency()) //
//          .createdOn(LocalDateTime.now()) //
//          .lastUpdatedOn(LocalDateTime.now()) //
//          .build());
//    });
//
//    return contracts;
//
//  }
//  

}
