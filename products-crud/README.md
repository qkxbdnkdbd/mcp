# Products CRUD

Spring Boot 商品 CRUD 示例项目，数据模型来自 `G:\chorm-downing\products.sql`。

## 环境

- JDK 8
- Maven 3.6+
- MySQL 5.7+ 或 8.x

## 初始化 MySQL

先创建数据库：

```sql
CREATE DATABASE products_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

再导入表结构和样例数据：

```powershell
mysql -uroot -p products_db < src/main/resources/schema.sql
mysql -uroot -p products_db < src/main/resources/data.sql
```

默认连接配置在 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/products_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
```

## 运行

```powershell
mvn spring-boot:run
```

服务启动后默认地址：

```text
http://localhost:8080
```

## 测试

```powershell
mvn test
```

测试环境使用 H2，初始化脚本在 `src/test/resources`。

## 接口

统一响应格式：

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

### 分页列表

```http
GET /api/products?page=0&size=10&keyword=ap&status=0
```

支持查询参数：`keyword`、`status`、`categoryId`、`supplierId`、`page`、`size`、`sort`。

### 详情

```http
GET /api/products/5
```

### 新增

```http
POST /api/products
Content-Type: application/json

{
  "productName": "测试商品",
  "barcode": "6900000000001",
  "purchasePrice": 12.30,
  "sellingPrice": 18.90,
  "stockQuantity": 20,
  "status": 1
}
```

### 修改

```http
PUT /api/products/5
Content-Type: application/json

{
  "productName": "更新后的商品",
  "barcode": "6900000000002",
  "purchasePrice": 20.00,
  "sellingPrice": 29.90,
  "stockQuantity": 99,
  "status": 1
}
```

### 修改状态

```http
PATCH /api/products/5/status
Content-Type: application/json

{
  "status": 0
}
```

### 删除

```http
DELETE /api/products/5
```
