package com.qww.products.service;

import com.qww.products.common.NotFoundException;
import com.qww.products.dto.ProductCreateRequest;
import com.qww.products.dto.ProductResponse;
import com.qww.products.dto.ProductUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void createsProductWithTimestampsAndDefaults() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setProductName("服务层新增商品");
        request.setBarcode("6900000000999");
        request.setPurchasePrice(new BigDecimal("12.30"));
        request.setSellingPrice(new BigDecimal("18.90"));

        ProductResponse response = productService.create(request);

        assertThat(response.getProductId()).isNotNull();
        assertThat(response.getProductName()).isEqualTo("服务层新增商品");
        assertThat(response.getPurchasePrice()).isEqualByComparingTo("12.30");
        assertThat(response.getSellingPrice()).isEqualByComparingTo("18.90");
        assertThat(response.getStockQuantity()).isZero();
        assertThat(response.getStatus()).isEqualTo(1);
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    void updatesProductAndRefreshesUpdatedAt() {
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setProductName("更新后的商品");
        request.setBarcode("6900000000888");
        request.setPurchasePrice(new BigDecimal("20.00"));
        request.setSellingPrice(new BigDecimal("29.90"));
        request.setStockQuantity(99);
        request.setStatus(0);

        ProductResponse response = productService.update(5L, request);

        assertThat(response.getProductId()).isEqualTo(5L);
        assertThat(response.getProductName()).isEqualTo("更新后的商品");
        assertThat(response.getBarcode()).isEqualTo("6900000000888");
        assertThat(response.getStockQuantity()).isEqualTo(99);
        assertThat(response.getStatus()).isZero();
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    void throwsWhenProductIsMissing() {
        assertThatThrownBy(() -> productService.getById(999999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void throwsWhenDeletingMissingProduct() {
        assertThatThrownBy(() -> productService.delete(999999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Product not found");
    }
}
