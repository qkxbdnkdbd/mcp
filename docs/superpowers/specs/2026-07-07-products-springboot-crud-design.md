# Products Spring Boot CRUD Design

## Goal

Create a single-module Spring Boot backend project for the `products` data in `G:\chorm-downing\products.sql`. The project exposes basic CRUD APIs, initializes a `products` table, and includes enough validation and tests to make the generated code usable as a starting point.

## Stack

- Java 8
- Spring Boot 2.7.x
- Maven
- Spring Web
- Spring Data JPA
- MySQL runtime driver
- H2 test database
- Lombok

## Data Model

The SQL file contains `INSERT` statements for one table, `products`, but no `CREATE TABLE` statement. The project will provide `schema.sql` and `data.sql`.

`Product` fields:

- `productId`: primary key, maps to `product_id`
- `productName`: required string
- `barcode`, `specification`, `brand`, `unit`, `imageUrl`, `description`, `createdBy`: optional strings
- `categoryId`, `supplierId`: optional integer IDs
- `purchasePrice`, `sellingPrice`: decimal values
- `stockQuantity`, `minStock`, `maxStock`, `status`: integer values
- `createdAt`, `updatedAt`: timestamps

Because the sample SQL contains mixed numeric and string-looking values in fields such as `category_id`, `supplier_id`, and `created_by`, the generated schema will keep `created_by` as a string and normalize blank category and supplier values to `NULL` in the seed data.

## API

Base path: `/api/products`

- `GET /api/products`: paged list with optional `keyword`, `status`, `categoryId`, and `supplierId`
- `GET /api/products/{id}`: get one product
- `POST /api/products`: create product
- `PUT /api/products/{id}`: update product
- `DELETE /api/products/{id}`: delete product
- `PATCH /api/products/{id}/status`: update only the status

Responses use a small unified wrapper with `code`, `message`, and `data`.

## Layers

- `controller`: HTTP API and request validation
- `service`: business rules, not-found handling, create/update timestamp handling
- `repository`: Spring Data JPA repository and query specifications
- `domain`: JPA entity
- `dto`: request and response DTOs
- `common`: response wrapper and global exception handler

## Error Handling

- Invalid request bodies return HTTP 400 with validation messages.
- Missing products return HTTP 404.
- Unexpected errors return HTTP 500 with a generic message.

## Testing

Add focused tests for:

- application context startup
- repository persistence against H2
- service create, update, and not-found behavior
- controller CRUD flow with MockMvc

## Out Of Scope

- Authentication and authorization
- Frontend pages
- File upload for product images
- Category and supplier tables
- Production Docker deployment
