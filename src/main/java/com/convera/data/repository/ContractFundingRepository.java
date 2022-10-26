package com.convera.data.repository;

import com.convera.data.repository.model.ContractFunding;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * contract Funding Repository.
 *
 * @Author: Sudarshan Datta
 */
public interface ContractFundingRepository extends CrudRepository<ContractFunding, String> {

  List<ContractFunding> findByContractId(String contractId);
}
