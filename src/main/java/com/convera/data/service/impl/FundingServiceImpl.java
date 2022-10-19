package com.convera.data.service.impl;

import com.convera.data.api.web.exception.DataNotFoundException;
import com.convera.data.api.web.model.request.ContractFundingRequestModel;
import com.convera.data.api.web.model.request.ContractSaveRequestModel;
import com.convera.data.api.web.model.request.FundingUpdateRequestModel;
import com.convera.data.api.web.model.request.OrderPersistRequestModel;
import com.convera.data.api.web.model.response.ContractFundingResponseModel;
import com.convera.data.api.web.model.response.ContractResponseModel;
import com.convera.data.api.web.model.response.OrderResponseModel;
import com.convera.data.repository.ContractFundingRepository;
import com.convera.data.repository.ContractRepository;
import com.convera.data.repository.OrderRepository;
import com.convera.data.repository.model.Contract;
import com.convera.data.repository.model.ContractFunding;
import com.convera.data.repository.model.Order;
import com.convera.data.service.FundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("FundingService")
public class FundingServiceImpl implements FundingService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    ContractFundingRepository contractFundingRepository;

    public Order insertFundingRecord(OrderPersistRequestModel orderPersistRequestModel)
    {
        Order order = new Order(orderPersistRequestModel.getOrderId(), orderPersistRequestModel.getOrderStatus(), orderPersistRequestModel.getFundingStatus(), LocalDateTime.now(ZoneOffset.UTC),LocalDateTime.now(ZoneOffset.UTC));
        Set<Contract> contractSet = getContractSet(orderPersistRequestModel.getContractList(),orderPersistRequestModel.getOrderId());

        orderRepository.save(order);
        contractRepository.saveAll(contractSet);

       // order.setContracts(contractSet);
        return order;

    }

    public Set<ContractFunding> insertContractFunding( FundingUpdateRequestModel fundingUpdateRequestModel)
    {
        Set<ContractFunding> contractFundingSet = new HashSet<>();

        for(ContractFundingRequestModel contractFundingRequestModel: fundingUpdateRequestModel.getContractFundingList())
        {
            ContractFunding conFund = new ContractFunding(UUID.randomUUID().toString(),
                    contractFundingRequestModel.getContractId(),contractFundingRequestModel.getFundingAmount(),
                    contractFundingRequestModel.getFundingDate(), contractFundingRequestModel.getFundingCurrency(),
                    contractFundingRequestModel.getBankRefNo(), LocalDateTime.now(),LocalDateTime.now());
            contractFundingSet.add(conFund);
        }
        contractFundingRepository.saveAll(contractFundingSet);
        return contractFundingSet;

    }

    @Override
    public List<ContractFunding> getContractFunding(String contractId) {
        return contractFundingRepository.findByContractId(contractId);
    }

    public OrderResponseModel getCompleteOrderByContractId(String contractId)
    {

        //Get all contracts based on orderId
        Optional<Contract> contract = contractRepository.findById(contractId);

        if(contract.isEmpty())
            throw new DataNotFoundException("Data not found in contract table: "+contractId);
        List<Contract> allContracts = contractRepository.findByOrderId(contract.get().getOrderId());

        //Get the order
        Optional<Order> order = orderRepository.findById(contract.get().getOrderId());

        //TODO: Represent all in single web response object
        OrderResponseModel orderResponseModel = transformToOrderResponseModel(order.get(),allContracts);
        return orderResponseModel;

    }

    private OrderResponseModel transformToOrderResponseModel(Order order, List<Contract> allContracts)

                                                            {

        OrderResponseModel orderResponseModel =
                OrderResponseModel.builder().orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                        .fundingStatus(order.getFundingStatus())
                        .contractResponseModels(transformToContractResponseModel(allContracts))
                        .build();

        return orderResponseModel;

    }

    private List<ContractResponseModel> transformToContractResponseModel(List<Contract> allContracts) {
        List<ContractResponseModel> contractResponseModels = new ArrayList<>();
        for(Contract contract : allContracts)
        {
           List<ContractFunding> contractFundingList = contractFundingRepository.findByContractId(contract.getContractId());

            List<ContractFundingResponseModel> contractFundingResponseModels = new ArrayList<>();
            ContractResponseModel contractResponseModel = ContractResponseModel.builder()
                    .contractId(contract.getContractId())
                    .contractFundings(transformToContractFundingResponseModel(contractFundingList))
                    .drawnDownAmount(contract.getDrawnDownAmount())
                    .tradeAmount(contract.getTradeAmount())
                    .tradeCurrency(contract.getTradeCurrency())
                    .contractId(contract.getContractId())
                    .orderId(contract.getOrderId())
                    .createdOn(contract.getCreatedOn())
                    .lastUpdatedOn(contract.getLastUpdatedOn())
                    .build();

            contractResponseModels.add(contractResponseModel);

        }

       return contractResponseModels;
    }

    private List<ContractFundingResponseModel> transformToContractFundingResponseModel(List<ContractFunding> contractFundingList) {
        List<ContractFundingResponseModel> contractFundingResponseModelList = new ArrayList<>();
        for(ContractFunding contractFunding : contractFundingList)
        {
            ContractFundingResponseModel contractFundingResponseModel = ContractFundingResponseModel.builder()
                    .bankRefNo(contractFunding.getBankRefNo())
                    .contractId(contractFunding.getContractId())
                    .fundingAmount(contractFunding.getFundingAmount())
                    .fundingCurrency(contractFunding.getFundingCurrency())
                    .fundingDate(contractFunding.getFundingDate())
                    .uuid(contractFunding.getUuid())
                    .createdOn(contractFunding.getCreatedOn())
                    .lastUpdatedOn(contractFunding.getLastUpdatedOn())
                    .build();
            contractFundingResponseModelList.add(contractFundingResponseModel);
        }

        return contractFundingResponseModelList;

    }

    private Set<Contract> getContractSet(List<ContractSaveRequestModel> contractSaveRequestModels, String orderId) {
        LinkedHashSet<Contract> contractRecs = null;

        if(CollectionUtils.isEmpty(contractSaveRequestModels))
            return null;

        contractRecs = new LinkedHashSet<>();
        for(ContractSaveRequestModel contractSaveRequestModel:contractSaveRequestModels)
        {
            Contract contract = new Contract(contractSaveRequestModel.getContractId(),orderId,
                    contractSaveRequestModel.getDrawnDownAmount(),contractSaveRequestModel.getTradeAmount(),
                    contractSaveRequestModel.getTradeCurrency(), LocalDateTime.now(),LocalDateTime.now());
            contractRecs.add(contract);
        }

        return contractRecs;

    }
}
