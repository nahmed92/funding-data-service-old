package com.convera.data.repository;

import com.convera.data.repository.model.Contract;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContractRepository extends CrudRepository<Contract,String> {
    List<Contract> findByOrderId(String orderId);
    
    List<Contract> findByOrderIdAndContractId(String orderId, String contractId);
    
}
