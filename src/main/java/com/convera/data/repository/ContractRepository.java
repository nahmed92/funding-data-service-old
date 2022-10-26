package com.convera.data.repository;

import com.convera.data.repository.model.Contract;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * contract Repository.
 *
 * @Author: Sudarshan Datta
 */
public interface ContractRepository extends CrudRepository<Contract, String> {
  List<Contract> findByOrderId(String orderId);

  List<Contract> findByOrderIdAndContractId(String orderId, String contractId);

}
