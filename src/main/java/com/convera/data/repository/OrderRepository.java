package com.convera.data.repository;

import com.convera.data.repository.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Order Repository.
 * 
 * @author Nasir Ahmed
 *
 */
@Repository
public interface OrderRepository extends CrudRepository<Order, String> {
}
