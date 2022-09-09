package com.convera.data.api.web;

import com.convera.data.repository.OrderRepository;
import com.convera.data.repository.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = FundingDataServiceController.class)
class FundingDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository productRepository;

    //@Test
    void getProduct_success() throws Exception {
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Order("NTR3113812", "MP-CPL-1", "funded","USD", Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)),Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)),new BigDecimal(1000),new BigDecimal(1000))));
        this.mockMvc.perform(get("/convera/funding/orders/NTR3113812"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 0,
                            "name": "first",
                            "description": "first product description",
                            "product_status": "available"
                        }""", false));
    }

 /*   @Test
    void getProduct_notFound() throws Exception {
        when(productRepository.findById(any()))
                .thenReturn(Optional.empty());
        this.mockMvc.perform(get("/convera/product/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveProduct_success() throws Exception {
        when(productRepository.save(any()))
                .thenReturn(new Order(0L, "first", "first product description", ProductStatus.AVAILABLE));
        this.mockMvc.perform(post("/convera/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 0,
                                    "name": "first",
                                    "description": "first product description",
                                    "product_status": "available"
                                }"""))
                .andExpect(status().isCreated())
                .andExpect(content().string("success"));
    }*/
}