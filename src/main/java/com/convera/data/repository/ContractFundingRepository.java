package com.convera.data.repository;

import com.convera.data.repository.model.ContractFunding;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContractFundingRepository extends CrudRepository<ContractFunding,String> {

    List<ContractFunding> findByContractId(String contractId);
}
