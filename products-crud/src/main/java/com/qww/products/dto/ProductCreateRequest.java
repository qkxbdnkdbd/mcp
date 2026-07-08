package com.qww.products.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class ProductCreateRequest {

    @NotBlank(message = "productName is required")
    private String productName;

    private String barcode;
    private String specification;
    private Integer categoryId;
    private String brand;

    @DecimalMin(value = "0.00", message = "purchasePrice must be non-negative")
    private BigDecimal purchasePrice;

    @DecimalMin(value = "0.00", message = "sellingPrice must be non-negative")
    private BigDecimal sellingPrice;

    private String unit;

    @Min(value = 0, message = "stockQuantity must be non-negative")
    private Integer stockQuantity;

    @Min(value = 0, message = "minStock must be non-negative")
    private Integer minStock;

    @Min(value = 0, message = "maxStock must be non-negative")
    private Integer maxStock;

    private String imageUrl;
    private String description;

    @Min(value = 0, message = "status must be non-negative")
    private Integer status;

    private String createdBy;
    private Integer supplierId;
}
