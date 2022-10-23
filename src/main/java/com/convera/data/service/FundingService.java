package com.convera.data.service;

import com.convera.data.api.web.model.request.FundingUpdateRequestModel;
import com.convera.data.api.web.model.request.OrderPersistRequestModel;
import com.convera.data.api.web.model.response.OrderResponseModel;
import com.convera.data.repository.model.Contract;
import com.convera.data.repository.model.ContractFunding;
import com.convera.data.repository.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FundingService {
  public Order createFundingOrderRecord(OrderPersistRequestModel orderPersistRequestModel);

  public Set<ContractFunding> insertContractFunding(FundingUpdateRequestModel fundingUpdateRequestModel);

  List<ContractFunding> getContractFunding(String contractId);

//    OrderResponseModel getCompleteOrderByContractId(String contractId);
  Optional<Order> findOrderById(String orderId);

  List<ContractFunding> getContractFundingByOrderIdAndContractId(String orderId, String contractId);

  List<Contract> getContractByOrderId(String orderId);
}
