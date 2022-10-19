package com.convera.data.service;

import com.convera.data.api.web.model.request.FundingUpdateRequestModel;
import com.convera.data.api.web.model.request.OrderPersistRequestModel;
import com.convera.data.api.web.model.response.OrderResponseModel;
import com.convera.data.repository.model.ContractFunding;
import com.convera.data.repository.model.Order;

import java.util.List;
import java.util.Set;

public interface FundingService {
    public Order insertFundingRecord(OrderPersistRequestModel orderPersistRequestModel);
    public  Set<ContractFunding> insertContractFunding(FundingUpdateRequestModel fundingUpdateRequestModel);

    public List<ContractFunding> getContractFunding(String contractId);
    public OrderResponseModel getCompleteOrderByContractId(String contractId);
}
