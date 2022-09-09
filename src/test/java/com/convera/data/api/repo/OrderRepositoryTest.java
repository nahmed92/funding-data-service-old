package com.convera.data.api.repo;

import com.convera.data.repository.OrderRepository;
import com.convera.data.repository.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private OrderRepository repository;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

  /*  @Test
    void testOrderPersistsAndFetch() {
        Order orderPersisted = new Order("NTR3113812", "MP-CPL-1", "funded","USD", Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)),Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)),new BigDecimal(1000),new BigDecimal(1000));

        em.persist(orderPersisted);

        Optional<Order> order = repository.findById("NTR3113812");
        Assertions.assertEquals(Boolean.TRUE, order.isPresent());
        Assertions.assertEquals("NTR3113812", order.get().getOrderId());
        Assertions.assertEquals("USD",order.get().getCurrency());
        Assertions.assertEquals(new BigDecimal(1000),order.get().getTotalAmount());


    }*/

    /*@Test
    void testOrderUpdate(){
        Order orderPersisted = new Order("NTR3113812", "MP-CPL-1", "pending","USD", Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)),Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)),new BigDecimal(1000),new BigDecimal(1000));
        em.persist(orderPersisted);
        Optional<Order> order = repository.findById("NTR3113812");
        Assertions.assertEquals("pending" , order.get().getStatus());
        order.get().setStatus("funded");
        repository.save(order.get());
        order = null;
        order = repository.findById("NTR3113812");
        Assertions.assertEquals("funded" , order.get().getStatus());
    }
*/

}
