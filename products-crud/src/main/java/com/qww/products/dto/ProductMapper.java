package com.qww.products.dto;

import com.qww.products.domain.Product;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .barcode(product.getBarcode())
                .specification(product.getSpecification())
                .categoryId(product.getCategoryId())
                .brand(product.getBrand())
                .purchasePrice(product.getPurchasePrice())
                .sellingPrice(product.getSellingPrice())
                .unit(product.getUnit())
                .stockQuantity(product.getStockQuantity())
                .minStock(product.getMinStock())
                .maxStock(product.getMaxStock())
                .imageUrl(product.getImageUrl())
                .description(product.getDescription())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .supplierId(product.getSupplierId())
                .build();
    }
}
