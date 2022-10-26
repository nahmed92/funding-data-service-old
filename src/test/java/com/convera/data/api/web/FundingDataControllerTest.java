
package com.convera.data.api.web;

import com.convera.data.api.utils.TestUtils;
import com.convera.data.repository.model.Contract;
import com.convera.data.repository.model.ContractFunding;
import com.convera.data.repository.model.Order;
import com.convera.data.service.FundingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FundingDataServiceController.class)
class FundingDataControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FundingService fundingService;

  @Autowired
  private ObjectMapper mapper;

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
  void souldGetContracts() throws Exception {
    String contractsText = TestUtils.getContentFromJsonFile("dataset/contract_list_data.json");
    List<ContractFunding> contracts = mapper.readValue(contractsText, List.class);
    when(fundingService.getContractFundingByOrderIdAndContractId(anyString(), anyString()))
        .thenReturn(contracts);
    this.mockMvc.perform(get("/convera/funding/orders/order_1/contracts/contract_1/contractsFunding/all"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", Matchers.is(contracts)));
  }

  @Test
  void shouldThrowContractsNotFound() throws Exception {
    doThrow(HttpClientErrorException.class).when(fundingService)
        .getContractFundingByOrderIdAndContractId(anyString(), anyString());
    String expectedResponse = TestUtils.getContentFromJsonFile("dataset/contracts_not_found.json");
    this.mockMvc.perform(get("/convera/funding/orders/NTR297612/contracts/contract_1/contractsFunding/all"))
        .andExpect(status().isNotFound())
        .andExpect(content().json(expectedResponse, false));
  }

  @Test
  void shouldThrowExceptionWhengetOrder() throws Exception {
    doThrow(InternalServerError.class).when(fundingService).getContractFundingByOrderIdAndContractId(anyString(), anyString());
    String orderRequest = TestUtils.getContentFromJsonFile("dataset/contract_list_data.json");
    this.mockMvc.perform(get("/convera/funding/orders/order_1/contracts/contract_1/contractsFunding/all").contentType(MediaType.APPLICATION_JSON).content(orderRequest))
        .andExpect(status().is5xxServerError());
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

  @Test
  void shouldSaveContractFunding() throws Exception {
    String orderRequest = TestUtils.getContentFromJsonFile("dataset/contractFunding_request.json");
    this.mockMvc.perform(post("/convera/funding/contractsFunding").contentType(MediaType.APPLICATION_JSON).content(orderRequest))
        .andExpect(status().isOk());
  }

  @Test
  void shouldThrowExceptionWhensaveContractFunding() throws Exception {
    doThrow(InternalServerError.class).when(fundingService).insertContractFunding(any());
    String orderRequest = TestUtils.getContentFromJsonFile("dataset/contractFunding_request.json");
    this.mockMvc.perform(post("/convera/funding/contractsFunding").contentType(MediaType.APPLICATION_JSON).content(orderRequest))
        .andExpect(status().is5xxServerError());
  }

  @Test
  void souldGetAllContractsByOrderId() throws Exception {
    String contractsText = TestUtils.getContentFromJsonFile("dataset/contract_all_list_data.json");
    List<Contract> contracts = mapper.readValue(contractsText, List.class);
    when(fundingService.getContractByOrderId(any()))
        .thenReturn(contracts);
    this.mockMvc.perform(get("/convera/funding//orders/order_1/contracts/all"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", Matchers.is(contracts)));
  }

  @Test
  void shouldThrowContractsNotFoundByOrderId() throws Exception {
    when(fundingService.getContractByOrderId(any())).thenReturn(any());
    String expectedResponse = TestUtils.getContentFromJsonFile("dataset/contracts_not_found_byOrderId.json");
    this.mockMvc.perform(get("/convera/funding//orders/NTR297612/contracts/all")).andExpect(status().isNotFound())
        .andExpect(content().json(expectedResponse, false));
  }
}