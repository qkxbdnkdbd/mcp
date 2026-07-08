package com.qww.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qww.products.dto.ProductCreateRequest;
import com.qww.products.dto.ProductStatusRequest;
import com.qww.products.dto.ProductUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listsProductsWithFilters() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("keyword", "ap")
                        .param("status", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.content[*].productName", containsInAnyOrder("apale", "apple1")));
    }

    @Test
    void getsProductById() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.productId", is(5)))
                .andExpect(jsonPath("$.data.productName", is("apple1")));
    }

    @Test
    void createsProduct() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setProductName("接口新增商品");
        request.setBarcode("6900000000777");
        request.setPurchasePrice(new BigDecimal("7.00"));
        request.setSellingPrice(new BigDecimal("9.90"));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.productId", greaterThan(0)))
                .andExpect(jsonPath("$.data.createdAt", notNullValue()))
                .andExpect(jsonPath("$.data.productName", is("接口新增商品")));
    }

    @Test
    void updatesProduct() throws Exception {
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setProductName("接口更新商品");
        request.setBarcode("6900000000666");
        request.setPurchasePrice(new BigDecimal("11.00"));
        request.setSellingPrice(new BigDecimal("22.00"));
        request.setStockQuantity(33);
        request.setStatus(1);

        mockMvc.perform(put("/api/products/{id}", 11L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.productId", is(11)))
                .andExpect(jsonPath("$.data.productName", is("接口更新商品")))
                .andExpect(jsonPath("$.data.stockQuantity", is(33)));
    }

    @Test
    void updatesProductStatus() throws Exception {
        ProductStatusRequest request = new ProductStatusRequest();
        request.setStatus(0);

        mockMvc.perform(patch("/api/products/{id}/status", 13L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.productId", is(13)))
                .andExpect(jsonPath("$.data.status", is(0)));
    }

    @Test
    void deletesProduct() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", 14L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data", is(true)));
    }

    @Test
    void returnsBadRequestForInvalidCreatePayload() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)));
    }

    @Test
    void returnsNotFoundForMissingProduct() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("Product not found: 999999")));
    }
}
