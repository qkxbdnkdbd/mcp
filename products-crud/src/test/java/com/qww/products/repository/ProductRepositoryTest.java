package com.qww.products.repository;

import com.qww.products.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void savesAndFindsProductById() {
        Product product = new Product();
        product.setProductName("测试商品");
        product.setBarcode("6900000000001");
        product.setBrand("测试品牌");
        product.setPurchasePrice(new BigDecimal("10.00"));
        product.setSellingPrice(new BigDecimal("15.50"));
        product.setStockQuantity(20);
        product.setMinStock(2);
        product.setMaxStock(50);
        product.setStatus(1);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = productRepository.save(product);

        assertThat(saved.getProductId()).isNotNull();
        assertThat(productRepository.findById(saved.getProductId()))
                .get()
                .extracting(Product::getProductName)
                .isEqualTo("测试商品");
    }

    @Test
    void searchesProductsByKeywordAndFilters() {
        Page<Product> page = productRepository.findAll(
                ProductRepository.buildSearchSpecification("ap", 0, null, null),
                PageRequest.of(0, 10)
        );

        assertThat(page.getContent())
                .extracting(Product::getProductName)
                .contains("apple1", "apale");
    }
}
