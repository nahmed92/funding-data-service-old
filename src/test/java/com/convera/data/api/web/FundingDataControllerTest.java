package com.convera.data.api.web;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = FundingDataServiceController.class)
class FundingDataControllerTest {
/**
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository productRepository;

    @Test
    void getProduct_success() throws Exception {
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Order(0L, "first", "first product description", ProductStatus.AVAILABLE)));
        this.mockMvc.perform(get("/convera/product/0"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 0,
                            "name": "first",
                            "description": "first product description",
                            "product_status": "available"
                        }""", false));
    }

    @Test
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
    } **/
}