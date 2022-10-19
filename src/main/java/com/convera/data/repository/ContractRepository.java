package com.convera.data.repository;

import com.convera.data.repository.model.Contract;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContractRepository extends CrudRepository<Contract,String> {
    public List<Contract> findByOrderId(String orderId);
}
