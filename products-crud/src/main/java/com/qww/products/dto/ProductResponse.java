package com.qww.products.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class ProductResponse {
    Long productId;
    String productName;
    String barcode;
    String specification;
    Integer categoryId;
    String brand;
    BigDecimal purchasePrice;
    BigDecimal sellingPrice;
    String unit;
    Integer stockQuantity;
    Integer minStock;
    Integer maxStock;
    String imageUrl;
    String description;
    Integer status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    Integer supplierId;
}
