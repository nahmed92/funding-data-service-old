
package com.convera.data.api.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.convera.data.api.utils.TestUtils;
import com.convera.data.repository.model.Contract;
import com.convera.data.repository.model.Order;
import com.convera.data.service.FundingService;

@WebMvcTest(controllers = FundingDataServiceController.class)
class FundingDataControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FundingService fundingService;

  LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

  @Test
  void shouldGetOrder() throws Exception {
    Set<Contract> contract = Set.of(Contract.builder()
        .contractId("contract1") //
        .orderId("order1") //
        .drawnDownAmount(new BigDecimal(300)) //
        .tradeAmount(new BigDecimal(30)) //
        .tradeCurrency("USD") //
        .build());
    Order order = Order.builder()
        .orderId("order1")
        .fundingStatus("PENDING") //
        .orderStatus("PENDING") //
        .contracts(contract) //
        .build();

    String expectedResponse = TestUtils.getContentFromJsonFile("dataset/order_fetch_response.json");
    when(fundingService.findOrderById(any()))
        .thenReturn(Optional.of(order));
    this.mockMvc.perform(get("/convera/funding/orders/order1")).andExpect(status().isOk())
        .andExpect(content().json(expectedResponse, false));
  }

  @Test
  void shouldThrowOrderNotFound() throws Exception {
    when(fundingService.findOrderById(any())).thenReturn(Optional.empty());
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
  void shouldThrow400SaveOrder() throws Exception {
    String orderRequest = "{\r\n"
        + "  \"orderId\": \"order_new_1\",\r\n"
        + "  \"orderStatus\": \"pendinf\",\r\n"
        + "  \"fundingStatus\": \"pending\",\r\n"
        + "  \"contractList\": [\r\n"
        + "    {\r\n"
        + "      \"contractId\": \"contract_new_1\",\r\n"
        + "      \"drawnDownAmount\": abc,\r\n"
        + "      \"tradeAmount\": 10,\r\n"
        + "      \"tradeCurrency\": \"USD\"\r\n"
        + "    }\r\n"
        + "  ]\r\n"
        + "}";
    this.mockMvc.perform(post("/convera/funding/orders").contentType(MediaType.APPLICATION_JSON).content(orderRequest))
            .andExpect(status().isBadRequest());
  }
  
  @Test
  void shouldThrowExceptionWhensaveOrder() throws Exception {
    doThrow(InternalServerError.class).when(fundingService).createFundingOrderRecord(any());
    String orderRequest = TestUtils.getContentFromJsonFile("dataset/order_request.json");
    this.mockMvc.perform(post("/convera/funding/orders").contentType(MediaType.APPLICATION_JSON).content(orderRequest))
        .andExpect(status().is5xxServerError());
  }
}