package com.qww.products.controller;

import com.qww.products.common.ApiResponse;
import com.qww.products.dto.ProductCreateRequest;
import com.qww.products.dto.ProductResponse;
import com.qww.products.dto.ProductStatusRequest;
import com.qww.products.dto.ProductUpdateRequest;
import com.qww.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Page<ProductResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer supplierId,
            @PageableDefault(size = 10, sort = "productId") Pageable pageable
    ) {
        return ApiResponse.success(productService.list(keyword, status, categoryId, supplierId, pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(productService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request) {
        return ApiResponse.success(productService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        return ApiResponse.success(productService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<ProductResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ProductStatusRequest request
    ) {
        return ApiResponse.success(productService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.success();
    }
}
