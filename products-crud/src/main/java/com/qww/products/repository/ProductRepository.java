package com.qww.products.repository;

import com.qww.products.domain.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    static Specification<Product> buildSearchSpecification(
            String keyword,
            Integer status,
            Integer categoryId,
            Integer supplierId
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                String likeKeyword = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), likeKeyword),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("barcode")), likeKeyword),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), likeKeyword)
                ));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), categoryId));
            }
            if (supplierId != null) {
                predicates.add(criteriaBuilder.equal(root.get("supplierId"), supplierId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
