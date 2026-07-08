package com.qww.products.service;

import com.qww.products.common.NotFoundException;
import com.qww.products.domain.Product;
import com.qww.products.dto.ProductCreateRequest;
import com.qww.products.dto.ProductMapper;
import com.qww.products.dto.ProductResponse;
import com.qww.products.dto.ProductStatusRequest;
import com.qww.products.dto.ProductUpdateRequest;
import com.qww.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponse> list(
            String keyword,
            Integer status,
            Integer categoryId,
            Integer supplierId,
            Pageable pageable
    ) {
        return productRepository.findAll(
                ProductRepository.buildSearchSpecification(keyword, status, categoryId, supplierId),
                pageable
        ).map(ProductMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return ProductMapper.toResponse(requireProduct(id));
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        Product product = new Product();
        applyCreateFields(product, request);
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Product product = requireProduct(id);
        applyUpdateFields(product, request);
        product.setUpdatedAt(LocalDateTime.now());
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateStatus(Long id, ProductStatusRequest request) {
        Product product = requireProduct(id);
        product.setStatus(request.getStatus());
        product.setUpdatedAt(LocalDateTime.now());
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        Product product = requireProduct(id);
        productRepository.delete(product);
    }

    private Product requireProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    private void applyCreateFields(Product product, ProductCreateRequest request) {
        product.setProductName(request.getProductName());
        product.setBarcode(request.getBarcode());
        product.setSpecification(request.getSpecification());
        product.setCategoryId(request.getCategoryId());
        product.setBrand(request.getBrand());
        product.setPurchasePrice(moneyOrZero(request.getPurchasePrice()));
        product.setSellingPrice(moneyOrZero(request.getSellingPrice()));
        product.setUnit(request.getUnit());
        product.setStockQuantity(intOrZero(request.getStockQuantity()));
        product.setMinStock(intOrZero(request.getMinStock()));
        product.setMaxStock(request.getMaxStock());
        product.setImageUrl(request.getImageUrl());
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus() == null ? 1 : request.getStatus());
        product.setCreatedBy(request.getCreatedBy());
        product.setSupplierId(request.getSupplierId());
    }

    private void applyUpdateFields(Product product, ProductUpdateRequest request) {
        product.setProductName(request.getProductName());
        product.setBarcode(request.getBarcode());
        product.setSpecification(request.getSpecification());
        product.setCategoryId(request.getCategoryId());
        product.setBrand(request.getBrand());
        product.setPurchasePrice(moneyOrZero(request.getPurchasePrice()));
        product.setSellingPrice(moneyOrZero(request.getSellingPrice()));
        product.setUnit(request.getUnit());
        product.setStockQuantity(intOrZero(request.getStockQuantity()));
        product.setMinStock(intOrZero(request.getMinStock()));
        product.setMaxStock(request.getMaxStock());
        product.setImageUrl(request.getImageUrl());
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus() == null ? 1 : request.getStatus());
        product.setCreatedBy(request.getCreatedBy());
        product.setSupplierId(request.getSupplierId());
    }

    private BigDecimal moneyOrZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private Integer intOrZero(Integer value) {
        return value == null ? 0 : value;
    }
}
