package com.qww.products.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProductStatusRequest {

    @NotNull(message = "status is required")
    @Min(value = 0, message = "status must be non-negative")
    private Integer status;
}
