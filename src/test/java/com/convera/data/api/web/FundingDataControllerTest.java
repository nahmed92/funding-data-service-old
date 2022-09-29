package com.convera.data.api.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.convera.data.api.utils.TestUtils;
import com.convera.data.repository.OrderRepository;
import com.convera.data.repository.model.Order;

@WebMvcTest(controllers = FundingDataServiceController.class)
class FundingDataControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderRepository orderRepository;

  LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

  @Test
  void shouldGetOrder() throws Exception {
    String expectedResponse = TestUtils.getContentFromJsonFile("dataset/order_fetch_response.json");
    when(orderRepository.findById(any()))
        .thenReturn(Optional.of(new Order("NTR297622", "MP-CPL-1", "COMMIT", "USD", new BigDecimal(34555), new BigDecimal(24000), now.of(2022, 9, 14, 16, 56, 30),
                now.of(2022, 9, 14, 16, 56, 30),"PENDING")));
    this.mockMvc.perform(get("/convera/funding/orders/NTR3113812")).andExpect(status().isOk())
        .andExpect(content().json(expectedResponse, false));
  }

  @Test
  void shouldThrowOrderNotFound() throws Exception {
    when(orderRepository.findById(any())).thenReturn(Optional.empty());
    String expectedResponse = TestUtils.getContentFromJsonFile("dataset/order_not_found.json");
    this.mockMvc.perform(get("/convera/funding/orders/NTR297612")).andExpect(status().isNotFound())
        .andExpect(content().json(expectedResponse, false));
  }

  @Test
  void shouldSaveOrder() throws Exception {
    String orderRequest = TestUtils.getContentFromJsonFile("dataset/order_request.json");
    this.mockMvc.perform(post("/convera/funding/orders").contentType(MediaType.APPLICATION_JSON).content(orderRequest))
        .andExpect(status().isOk());
  }

  @Test
  void shouldThrowExceptionWhensaveOrder() throws Exception {
    doThrow(InternalServerError.class).when(orderRepository).save(any());
    String orderRequest = TestUtils.getContentFromJsonFile("dataset/order_request.json");
    this.mockMvc.perform(post("/convera/funding/orders").contentType(MediaType.APPLICATION_JSON).content(orderRequest))
        .andExpect(status().is5xxServerError());
  }

  @Test
  void shouldUpdateOrder() throws Exception {
    Order order = new Order("NTR297622", "MP-CPL-1", "Pending", "USD", new BigDecimal(34555), new BigDecimal(24000) , now.of(2022, 9, 14, 16, 56, 30),
            now.of(2022, 9, 14, 16, 56, 30),"FUNDED");
    when(orderRepository.findById("NTR297622")).thenReturn(Optional.of(order));

    String updateRequest = TestUtils.getContentFromJsonFile("dataset/update_order_request.json");

    when(orderRepository.save(order)).thenReturn(new Order("NTR297622", "MP-CPL-1", "Funded", "USD",
         new BigDecimal(34555), new BigDecimal(3100),now.of(2022, 9, 14, 16, 56, 30), now.of(2022, 9, 14, 16, 56, 30), "FUNDED"));
    this.mockMvc
        .perform(
            patch("/convera/funding/orders/NTR297622").contentType(MediaType.APPLICATION_JSON).content(updateRequest))
        .andExpect(status().isOk());
  }

  @Test
  void shouldThrowNotFoundExceptionWhenUpdateOrderNotFound() throws Exception {
    when(orderRepository.findById("NTR297612")).thenReturn(Optional.empty());
    String updateRequest = TestUtils.getContentFromJsonFile("dataset/update_order_request.json");
    String expected = TestUtils.getContentFromJsonFile("dataset/order_not_found.json");
    this.mockMvc
        .perform(
            patch("/convera/funding/orders/NTR297612").contentType(MediaType.APPLICATION_JSON).content(updateRequest))
        .andExpect(status().isNotFound()).andExpect(content().json(expected, false));

  }

  @Test
  void shouldThrowExceptionWhenUpdateOrder() throws Exception {
    doThrow(InternalServerError.class).when(orderRepository).findById("TR297622");
    String updateRequest = TestUtils.getContentFromJsonFile("dataset/update_order_request.json");
    this.mockMvc
        .perform(
            patch("/convera/funding/orders/TR297622").contentType(MediaType.APPLICATION_JSON).content(updateRequest))
        .andExpect(status().is5xxServerError());
  }

  @Test
  void shouldThrowHttpMessageNotReadableExceptionWhenUpdateOrder() throws Exception {
    String updateRequest = TestUtils.getContentFromJsonFile("dataset/update_order_exception_request.json");
    this.mockMvc
            .perform(
                    patch("/convera/funding/orders/TR297622").contentType(MediaType.APPLICATION_JSON).content(updateRequest))
            .andExpect(status().isBadRequest());
  }
}