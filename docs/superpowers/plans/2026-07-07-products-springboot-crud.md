# Products Spring Boot CRUD Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a runnable Spring Boot backend project that exposes CRUD APIs for the `products` table represented by `G:\chorm-downing\products.sql`.

**Architecture:** Create a single Maven module under `products-crud` with a conventional controller-service-repository split. JPA owns persistence, DTOs isolate HTTP payloads from the entity, and SQL initialization files provide schema plus seed data.

**Tech Stack:** Java 8, Spring Boot 2.7.x, Maven, Spring Web, Spring Data JPA, MySQL runtime driver, H2 tests, Lombok.

## Global Constraints

- Java version is 8.
- Project directory is `products-crud`.
- Base package is `com.qww.products`.
- API base path is `/api/products`.
- Seed data must come from `G:\chorm-downing\products.sql`, normalized into project-local SQL files.
- No authentication, frontend, file upload, category table, supplier table, or Docker deployment.

---

## File Structure

- `products-crud/pom.xml`: Maven build and dependencies.
- `products-crud/src/main/java/com/qww/products/ProductsApplication.java`: Spring Boot entrypoint.
- `products-crud/src/main/java/com/qww/products/domain/Product.java`: JPA entity mapped to `products`.
- `products-crud/src/main/java/com/qww/products/dto/ProductCreateRequest.java`: create payload.
- `products-crud/src/main/java/com/qww/products/dto/ProductUpdateRequest.java`: full update payload.
- `products-crud/src/main/java/com/qww/products/dto/ProductStatusRequest.java`: status-only update payload.
- `products-crud/src/main/java/com/qww/products/dto/ProductResponse.java`: response payload.
- `products-crud/src/main/java/com/qww/products/dto/ProductMapper.java`: entity/DTO mapping.
- `products-crud/src/main/java/com/qww/products/repository/ProductRepository.java`: Spring Data repository and specifications.
- `products-crud/src/main/java/com/qww/products/service/ProductService.java`: CRUD use cases.
- `products-crud/src/main/java/com/qww/products/common/ApiResponse.java`: unified response wrapper.
- `products-crud/src/main/java/com/qww/products/common/GlobalExceptionHandler.java`: validation, not-found, and fallback errors.
- `products-crud/src/main/java/com/qww/products/common/NotFoundException.java`: domain not-found error.
- `products-crud/src/main/java/com/qww/products/controller/ProductController.java`: REST endpoints.
- `products-crud/src/main/resources/application.yml`: default MySQL-oriented config.
- `products-crud/src/main/resources/schema.sql`: table schema.
- `products-crud/src/main/resources/data.sql`: seed data normalized from source SQL.
- `products-crud/src/test/resources/application-test.yml`: H2 test config.
- `products-crud/src/test/java/com/qww/products/ProductsApplicationTests.java`: context test.
- `products-crud/src/test/java/com/qww/products/repository/ProductRepositoryTest.java`: repository test.
- `products-crud/src/test/java/com/qww/products/service/ProductServiceTest.java`: service tests.
- `products-crud/src/test/java/com/qww/products/controller/ProductControllerTest.java`: MockMvc API tests.
- `products-crud/README.md`: run instructions and endpoint examples.

### Task 1: Project Scaffold And SQL Initialization

**Files:**
- Create: `products-crud/pom.xml`
- Create: `products-crud/src/main/java/com/qww/products/ProductsApplication.java`
- Create: `products-crud/src/main/resources/application.yml`
- Create: `products-crud/src/main/resources/schema.sql`
- Create: `products-crud/src/main/resources/data.sql`
- Create: `products-crud/src/test/resources/application-test.yml`
- Create: `products-crud/src/test/java/com/qww/products/ProductsApplicationTests.java`

**Interfaces:**
- Produces: Maven project with `ProductsApplication` and database initialization files.

- [ ] **Step 1: Write the context test**

```java
package com.qww.products;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProductsApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

- [ ] **Step 2: Add project build and app entrypoint**

Create `pom.xml` with Spring Boot web, data-jpa, validation, MySQL, H2, Lombok, and test dependencies. Create `ProductsApplication` with `SpringApplication.run(ProductsApplication.class, args)`.

- [ ] **Step 3: Add database config and initialization SQL**

`schema.sql` creates the `products` table. `data.sql` inserts normalized records from `G:\chorm-downing\products.sql`; blank numeric IDs are stored as `NULL`.

- [ ] **Step 4: Run startup test**

Run: `mvn test -Dtest=ProductsApplicationTests`

Expected: build succeeds and the Spring context starts with H2.

### Task 2: Domain, Repository, And Mapper

**Files:**
- Create: `products-crud/src/main/java/com/qww/products/domain/Product.java`
- Create: `products-crud/src/main/java/com/qww/products/repository/ProductRepository.java`
- Create: `products-crud/src/main/java/com/qww/products/dto/ProductResponse.java`
- Create: `products-crud/src/main/java/com/qww/products/dto/ProductMapper.java`
- Create: `products-crud/src/test/java/com/qww/products/repository/ProductRepositoryTest.java`

**Interfaces:**
- Consumes: `products` table from Task 1.
- Produces: `ProductRepository search(String keyword, Integer status, Integer categoryId, Integer supplierId, Pageable pageable)` support through a default `Specification<Product>` builder and `ProductMapper.toResponse(Product product)`.

- [ ] **Step 1: Write repository persistence test**

Test saving a product, finding it by id, and searching by keyword.

- [ ] **Step 2: Implement entity and repository**

Map all SQL columns with `@Column(name = "...")`, use `BigDecimal` for prices, `Integer` for status and stock values, and `OffsetDateTime` for timestamps.

- [ ] **Step 3: Implement response mapper**

Expose all product fields in `ProductResponse`; mapping is field-for-field and has no side effects.

- [ ] **Step 4: Run repository test**

Run: `mvn test -Dtest=ProductRepositoryTest`

Expected: repository test passes on H2.

### Task 3: Service CRUD Behavior

**Files:**
- Create: `products-crud/src/main/java/com/qww/products/dto/ProductCreateRequest.java`
- Create: `products-crud/src/main/java/com/qww/products/dto/ProductUpdateRequest.java`
- Create: `products-crud/src/main/java/com/qww/products/dto/ProductStatusRequest.java`
- Create: `products-crud/src/main/java/com/qww/products/common/NotFoundException.java`
- Create: `products-crud/src/main/java/com/qww/products/service/ProductService.java`
- Create: `products-crud/src/test/java/com/qww/products/service/ProductServiceTest.java`

**Interfaces:**
- Consumes: `ProductRepository`, `ProductMapper`.
- Produces: `ProductService.list(...)`, `getById(Long id)`, `create(ProductCreateRequest request)`, `update(Long id, ProductUpdateRequest request)`, `delete(Long id)`, and `updateStatus(Long id, ProductStatusRequest request)`.

- [ ] **Step 1: Write service tests**

Test create sets timestamps, update changes fields, get missing id throws `NotFoundException`, and delete missing id throws `NotFoundException`.

- [ ] **Step 2: Implement request DTOs**

Use validation annotations: `productName` is required, prices are non-negative, and stock/status values are non-negative when present.

- [ ] **Step 3: Implement service**

Use repository lookups for all id-based operations. For create, set both timestamps to `OffsetDateTime.now()`. For update, preserve `createdAt` and refresh `updatedAt`.

- [ ] **Step 4: Run service tests**

Run: `mvn test -Dtest=ProductServiceTest`

Expected: service tests pass.

### Task 4: REST Controller And Error Handling

**Files:**
- Create: `products-crud/src/main/java/com/qww/products/common/ApiResponse.java`
- Create: `products-crud/src/main/java/com/qww/products/common/GlobalExceptionHandler.java`
- Create: `products-crud/src/main/java/com/qww/products/controller/ProductController.java`
- Create: `products-crud/src/test/java/com/qww/products/controller/ProductControllerTest.java`

**Interfaces:**
- Consumes: `ProductService`.
- Produces: REST endpoints under `/api/products` and response wrapper `ApiResponse<T>`.

- [ ] **Step 1: Write MockMvc tests**

Test list, get one, create, update, patch status, delete, validation failure, and missing id response.

- [ ] **Step 2: Implement controller**

Expose `GET`, `POST`, `PUT`, `DELETE`, and `PATCH /{id}/status`, returning `ApiResponse.success(...)`.

- [ ] **Step 3: Implement exception handler**

Map `MethodArgumentNotValidException` to HTTP 400, `NotFoundException` to HTTP 404, and other runtime errors to HTTP 500.

- [ ] **Step 4: Run controller tests**

Run: `mvn test -Dtest=ProductControllerTest`

Expected: all controller tests pass.

### Task 5: Documentation And Full Verification

**Files:**
- Create: `products-crud/README.md`

**Interfaces:**
- Consumes: all implemented endpoints and configuration files.
- Produces: concise run instructions and curl examples.

- [ ] **Step 1: Write README**

Document Java 17, Maven, MySQL database configuration, test command, run command, and endpoint examples.

- [ ] **Step 2: Run all tests**

Run: `mvn test`

Expected: all tests pass.

- [ ] **Step 3: Package without tests skipped**

Run: `mvn package`

Expected: jar builds successfully in `target`.

## Self-Review

- Spec coverage: scaffold, schema, seed data, CRUD endpoints, validation, errors, tests, and README are covered by Tasks 1-5.
- Placeholder scan: no placeholder terms are intentionally left for implementation.
- Type consistency: service, controller, DTO, repository, and mapper names are consistent across tasks.
