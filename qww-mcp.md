# qww-test API 自动化测试报告

> **模块**：qww-test（Products CRUD）
> **数据源**：Apifox OpenAPI（`openapi.json`）
> **Base URL**：`http://localhost:8080`
> **测试时间**：2026-07-07T19:46:08.279452

## 测试摘要

| 指标 | 数值 |
|------|------|
| 接口总数 | 6 |
| 通过 | 1 |
| 失败 | 5 |

## 接口清单

| # | 接口名称 | 方法 | 路径 | 结果 |
|---|----------|------|------|------|
| 1 | 商品列表 list | GET | `/api/products` | ❌ 失败 |
| 2 | 创建商品 create | POST | `/api/products` | ❌ 失败 |
| 3 | 商品详情 getById | GET | `/api/products/25` | ❌ 失败 |
| 4 | 更新商品 update | PUT | `/api/products/25` | ❌ 失败 |
| 5 | 更新状态 updateStatus | PATCH | `/api/products/25/status` | ❌ 失败 |
| 6 | 删除商品 delete | DELETE | `/api/products/25` | ✅ 通过 |

---

## 详细测试结果

### 🐛 [Auto-Test] 商品列表 list - 200 (Schema不匹配)

**标签**：`bug` `auto-test`

**失败原因**：HTTP 状态码为 2xx，但响应不符合 JSON Schema

#### 请求 URL

`GET http://localhost:8080/api/products`

#### 请求参数

```json
{
  "keyword": "ap",
  "status": 0,
  "size": 10
}
```

#### 实际响应报文

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "content": [
      {
        "productId": 4,
        "productName": "apale",
        "barcode": "3",
        "specification": null,
        "categoryId": null,
        "brand": null,
        "purchasePrice": 1.0,
        "sellingPrice": 2.0,
        "unit": "",
        "stockQuantity": 0,
        "minStock": 0,
        "maxStock": null,
        "imageUrl": null,
        "description": null,
        "status": 0,
        "createdAt": "2026-05-26T11:29:44",
        "updatedAt": "2026-05-26T11:29:44",
        "createdBy": null,
        "supplierId": null
      },
      {
        "productId": 5,
        "productName": "apple1",
        "barcode": "4",
        "specification": null,
        "categoryId": null,
        "brand": null,
        "purchasePrice": 1.0,
        "sellingPrice": 2.0,
        "unit": "",
        "stockQuantity": 0,
        "minStock": 0,
        "maxStock": null,
        "imageUrl": null,
        "description": null,
        "status": 0,
        "createdAt": "2026-05-26T11:29:45",
        "updatedAt": "2026-05-26T11:29:45",
        "createdBy": null,
        "supplierId": null
      }
    ],
    "pageable": {
      "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
      },
      "offset": 0,
      "pageNumber": 0,
      "pageSize": 10,
      "paged": true,
      "unpaged": false
    },
    "last": true,
    "totalElements": 2,
    "totalPages": 1,
    "number": 0,
    "size": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "numberOfElements": 2,
    "first": true,
    "empty": false
  }
}
```

#### 预期响应

- **HTTP 状态码**：200（2xx）
- **JSON Schema**：

```json
{
  "type": "object",
  "properties": {
    "code": {
      "type": "integer",
      "description": ""
    },
    "message": {
      "type": "string",
      "description": ""
    },
    "data": {
      "type": "object",
      "properties": {
        "content": {
          "type": "array",
          "description": "",
          "items": {
            "type": "object",
            "properties": {
              "productId": {
                "type": "integer",
                "description": "",
                "format": "int64"
              },
              "productName": {
                "type": "string",
                "description": ""
              },
              "barcode": {
                "type": "string",
                "description": ""
              },
              "specification": {
                "type": "string",
                "description": ""
              },
              "categoryId": {
                "type": "integer",
                "description": ""
              },
              "brand": {
                "type": "string",
                "description": ""
              },
              "purchasePrice": {
                "type": "number",
                "description": ""
              },
              "sellingPrice": {
                "type": "number",
                "description": ""
              },
              "unit": {
                "type": "string",
                "description": ""
              },
              "stockQuantity": {
                "type": "integer",
                "description": ""
              },
              "minStock": {
                "type": "integer",
                "description": ""
              },
              "maxStock": {
                "type": "integer",
                "description": ""
              },
              "imageUrl": {
                "type": "string",
                "description": ""
              },
              "description": {
                "type": "string",
                "description": ""
              },
              "status": {
                "type": "integer",
                "description": ""
              },
              "createdAt": {
                "type": "string",
                "description": ""
              },
              "updatedAt": {
                "type": "string",
                "description": ""
              },
              "createdBy": {
                "type": "string",
                "description": ""
              },
              "supplierId": {
                "type": "integer",
                "description": ""
              }
            },
            "description": "com.qww.products.dto.ProductResponse"
          }
        },
        "pageable": {
          "type": "object",
          "properties": {
            "paged": {
              "type": "boolean"
            },
            "unpaged": {
              "type": "boolean"
            },
            "pageNumber": {
              "type": "integer"
            },
            "pageSize": {
              "type": "integer"
            },
            "offset": {
              "type": "integer",
              "format": "int64"
            },
            "sort": {
              "type": "array",
              "items": {
                "type": "object",
                "properties": {
                  "direction": {
                    "type": "string",
                    "description": "{ASC=ASC, DESC=DESC}",
                    "enum": [
                      "ASC",
                      "DESC"
                    ]
                  },
                  "property": {
                    "type": "string",
                    "description": ""
                  },
                  "ignoreCase": {
                    "type": "boolean",
                    "description": ""
                  },
                  "nullHandling": {
                    "type": "string",
                    "description": "{NATIVE=NATIVE, NULLS_FIRST=NULLS_FIRST, NULLS_LAST=NULLS_LAST}",
                    "enum": [
                      "NATIVE",
                      "NULLS_FIRST",
                      "NULLS_LAST"
                    ]
                  },
                  "ascending": {
                    "type": "boolean"
                  },
                  "descending": {
                    "type": "boolean"
                  }
                },
                "description": "org.springframework.data.domain.Sort"
              }
            }
          },
          "description": ""
        },
        "total": {
          "type": "integer",
          "description": "",
          "format": "int64"
        },
        "empty": {
          "type": "boolean"
        },
        "number": {
          "type": "integer"
        },
        "size": {
          "type": "integer"
        },
        "numberOfElements": {
          "type": "integer"
        },
        "sort": {
          "type": "array",
          "items": {
            "type": "object",
            "properties": {
              "direction": {
                "type": "string",
                "description": "{ASC=ASC, DESC=DESC}",
                "enum": [
                  "ASC",
                  "DESC"
                ]
              },
              "property": {
                "type": "string",
                "description": ""
              },
              "ignoreCase": {
                "type": "boolean",
                "description": ""
              },
              "nullHandling": {
                "type": "string",
                "description": "{NATIVE=NATIVE, NULLS_FIRST=NULLS_FIRST, NULLS_LAST=NULLS_LAST}",
                "enum": [
                  "NATIVE",
                  "NULLS_FIRST",
                  "NULLS_LAST"
                ]
              },
              "ascending": {
                "type": "boolean"
              },
              "descending": {
                "type": "boolean"
              }
            },
            "description": "org.springframework.data.domain.Sort"
          }
        },
        "first": {
          "type": "boolean"
        },
        "last": {
          "type": "boolean"
        },
        "totalPages": {
          "type": "integer"
        },
        "totalElements": {
          "type": "integer",
          "format": "int64"
        }
      },
      "description": ""
    }
  }
}
```

**Schema 校验错误**：
- `$.data.content[0].specification: expected string`
- `$.data.content[0].categoryId: expected integer, got NoneType`
- `$.data.content[0].brand: expected string`
- `$.data.content[0].maxStock: expected integer, got NoneType`
- `$.data.content[0].imageUrl: expected string`
- `$.data.content[0].description: expected string`
- `$.data.content[0].createdBy: expected string`
- `$.data.content[0].supplierId: expected integer, got NoneType`
- `$.data.content[1].specification: expected string`
- `$.data.content[1].categoryId: expected integer, got NoneType`
- `$.data.content[1].brand: expected string`
- `$.data.content[1].maxStock: expected integer, got NoneType`
- `$.data.content[1].imageUrl: expected string`
- `$.data.content[1].description: expected string`
- `$.data.content[1].createdBy: expected string`
- `$.data.content[1].supplierId: expected integer, got NoneType`
- `$.data.pageable.sort: expected array`
- `$.data.sort: expected array`

#### 数据库快照（products 表）

```json
[
  {
    "product_id": 4,
    "product_name": "apale",
    "barcode": "3",
    "status": 0,
    "stock_quantity": 0,
    "updated_at": "2026-05-26 11:29:44"
  },
  {
    "product_id": 5,
    "product_name": "apple1",
    "barcode": "4",
    "status": 0,
    "stock_quantity": 0,
    "updated_at": "2026-05-26 11:29:45"
  },
  {
    "product_id": 11,
    "product_name": "农夫山泉 550ml",
    "barcode": "6901122334455",
    "status": 1,
    "stock_quantity": 500,
    "updated_at": "2026-05-26 06:10:54"
  },
  {
    "product_id": 13,
    "product_name": "华为蓝牙耳机 FreeBuds 5",
    "barcode": "6903344556677",
    "status": 1,
    "stock_quantity": 25,
    "updated_at": "2026-05-26 06:10:54"
  },
  {
    "product_id": 14,
    "product_name": "得力固体胶 12g",
    "barcode": "6904455667788",
    "status": 1,
    "stock_quantity": 400,
    "updated_at": "2026-05-26 06:10:54"
  }
]
```

---

### 🐛 [Auto-Test] 创建商品 create - 201 (Schema不匹配)

**标签**：`bug` `auto-test`

**失败原因**：HTTP 状态码为 2xx，但响应不符合 JSON Schema

#### 请求 URL

`POST http://localhost:8080/api/products`

#### 请求参数

```json
{
  "productName": "AutoTest-bd02ac34",
  "barcode": "6900000000999",
  "purchasePrice": 7.0,
  "sellingPrice": 9.9
}
```

#### 实际响应报文

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "productId": 25,
    "productName": "AutoTest-bd02ac34",
    "barcode": "6900000000999",
    "specification": null,
    "categoryId": null,
    "brand": null,
    "purchasePrice": 7.0,
    "sellingPrice": 9.9,
    "unit": null,
    "stockQuantity": 0,
    "minStock": 0,
    "maxStock": null,
    "imageUrl": null,
    "description": null,
    "status": 1,
    "createdAt": "2026-07-07T19:46:07.006",
    "updatedAt": "2026-07-07T19:46:07.006",
    "createdBy": null,
    "supplierId": null
  }
}
```

#### 预期响应

- **HTTP 状态码**：200（2xx）
- **JSON Schema**：

```json
{
  "type": "object",
  "properties": {
    "code": {
      "type": "integer",
      "description": ""
    },
    "message": {
      "type": "string",
      "description": ""
    },
    "data": {
      "type": "object",
      "properties": {
        "productId": {
          "type": "integer",
          "description": "",
          "format": "int64"
        },
        "productName": {
          "type": "string",
          "description": ""
        },
        "barcode": {
          "type": "string",
          "description": ""
        },
        "specification": {
          "type": "string",
          "description": ""
        },
        "categoryId": {
          "type": "integer",
          "description": ""
        },
        "brand": {
          "type": "string",
          "description": ""
        },
        "purchasePrice": {
          "type": "number",
          "description": ""
        },
        "sellingPrice": {
          "type": "number",
          "description": ""
        },
        "unit": {
          "type": "string",
          "description": ""
        },
        "stockQuantity": {
          "type": "integer",
          "description": ""
        },
        "minStock": {
          "type": "integer",
          "description": ""
        },
        "maxStock": {
          "type": "integer",
          "description": ""
        },
        "imageUrl": {
          "type": "string",
          "description": ""
        },
        "description": {
          "type": "string",
          "description": ""
        },
        "status": {
          "type": "integer",
          "description": ""
        },
        "createdAt": {
          "type": "string",
          "description": ""
        },
        "updatedAt": {
          "type": "string",
          "description": ""
        },
        "createdBy": {
          "type": "string",
          "description": ""
        },
        "supplierId": {
          "type": "integer",
          "description": ""
        }
      },
      "description": ""
    }
  }
}
```

**Schema 校验错误**：
- `$.data.specification: expected string`
- `$.data.categoryId: expected integer, got NoneType`
- `$.data.brand: expected string`
- `$.data.unit: expected string`
- `$.data.maxStock: expected integer, got NoneType`
- `$.data.imageUrl: expected string`
- `$.data.description: expected string`
- `$.data.createdBy: expected string`
- `$.data.supplierId: expected integer, got NoneType`

#### 数据库快照（products 表）

```json
[
  {
    "product_id": 4,
    "product_name": "apale",
    "barcode": "3",
    "status": 0,
    "stock_quantity": 0,
    "updated_at": "2026-05-26 11:29:44"
  },
  {
    "product_id": 5,
    "product_name": "apple1",
    "barcode": "4",
    "status": 0,
    "stock_quantity": 0,
    "updated_at": "2026-05-26 11:29:45"
  },
  {
    "product_id": 11,
    "product_name": "农夫山泉 550ml",
    "barcode": "6901122334455",
    "status": 1,
    "stock_quantity": 500,
    "updated_at": "2026-05-26 06:10:54"
  },
  {
    "product_id": 13,
    "product_name": "华为蓝牙耳机 FreeBuds 5",
    "barcode": "6903344556677",
    "status": 1,
    "stock_quantity": 25,
    "updated_at": "2026-05-26 06:10:54"
  },
  {
    "product_id": 14,
    "product_name": "得力固体胶 12g",
    "barcode": "6904455667788",
    "status": 1,
    "stock_quantity": 400,
    "updated_at": "2026-05-26 06:10:54"
  }
]
```

---

### 🐛 [Auto-Test] 商品详情 getById - 200 (Schema不匹配)

**标签**：`bug` `auto-test`

**失败原因**：HTTP 状态码为 2xx，但响应不符合 JSON Schema

#### 请求 URL

`GET http://localhost:8080/api/products/25`

#### 请求参数

```json
{
  "id": 25
}
```

#### 实际响应报文

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "productId": 25,
    "productName": "AutoTest-bd02ac34",
    "barcode": "6900000000999",
    "specification": null,
    "categoryId": null,
    "brand": null,
    "purchasePrice": 7.0,
    "sellingPrice": 9.9,
    "unit": null,
    "stockQuantity": 0,
    "minStock": 0,
    "maxStock": null,
    "imageUrl": null,
    "description": null,
    "status": 1,
    "createdAt": "2026-07-07T19:46:07",
    "updatedAt": "2026-07-07T19:46:07",
    "createdBy": null,
    "supplierId": null
  }
}
```

#### 预期响应

- **HTTP 状态码**：200（2xx）
- **JSON Schema**：

```json
{
  "type": "object",
  "properties": {
    "code": {
      "type": "integer",
      "description": ""
    },
    "message": {
      "type": "string",
      "description": ""
    },
    "data": {
      "type": "object",
      "properties": {
        "productId": {
          "type": "integer",
          "description": "",
          "format": "int64"
        },
        "productName": {
          "type": "string",
          "description": ""
        },
        "barcode": {
          "type": "string",
          "description": ""
        },
        "specification": {
          "type": "string",
          "description": ""
        },
        "categoryId": {
          "type": "integer",
          "description": ""
        },
        "brand": {
          "type": "string",
          "description": ""
        },
        "purchasePrice": {
          "type": "number",
          "description": ""
        },
        "sellingPrice": {
          "type": "number",
          "description": ""
        },
        "unit": {
          "type": "string",
          "description": ""
        },
        "stockQuantity": {
          "type": "integer",
          "description": ""
        },
        "minStock": {
          "type": "integer",
          "description": ""
        },
        "maxStock": {
          "type": "integer",
          "description": ""
        },
        "imageUrl": {
          "type": "string",
          "description": ""
        },
        "description": {
          "type": "string",
          "description": ""
        },
        "status": {
          "type": "integer",
          "description": ""
        },
        "createdAt": {
          "type": "string",
          "description": ""
        },
        "updatedAt": {
          "type": "string",
          "description": ""
        },
        "createdBy": {
          "type": "string",
          "description": ""
        },
        "supplierId": {
          "type": "integer",
          "description": ""
        }
      },
      "description": ""
    }
  }
}
```

**Schema 校验错误**：
- `$.data.specification: expected string`
- `$.data.categoryId: expected integer, got NoneType`
- `$.data.brand: expected string`
- `$.data.unit: expected string`
- `$.data.maxStock: expected integer, got NoneType`
- `$.data.imageUrl: expected string`
- `$.data.description: expected string`
- `$.data.createdBy: expected string`
- `$.data.supplierId: expected integer, got NoneType`

#### 数据库快照（products 表）

```json
[]
```

---

### 🐛 [Auto-Test] 更新商品 update - 200 (Schema不匹配)

**标签**：`bug` `auto-test`

**失败原因**：HTTP 状态码为 2xx，但响应不符合 JSON Schema

#### 请求 URL

`PUT http://localhost:8080/api/products/25`

#### 请求参数

```json
{
  "id": 25,
  "productName": "接口更新商品-AutoTest",
  "barcode": "6900000000666",
  "purchasePrice": 11.0,
  "sellingPrice": 22.0,
  "stockQuantity": 33,
  "status": 1
}
```

#### 实际响应报文

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "productId": 25,
    "productName": "接口更新商品-AutoTest",
    "barcode": "6900000000666",
    "specification": null,
    "categoryId": null,
    "brand": null,
    "purchasePrice": 11.0,
    "sellingPrice": 22.0,
    "unit": null,
    "stockQuantity": 33,
    "minStock": 0,
    "maxStock": null,
    "imageUrl": null,
    "description": null,
    "status": 1,
    "createdAt": "2026-07-07T19:46:07",
    "updatedAt": "2026-07-07T19:46:07.639",
    "createdBy": null,
    "supplierId": null
  }
}
```

#### 预期响应

- **HTTP 状态码**：200（2xx）
- **JSON Schema**：

```json
{
  "type": "object",
  "properties": {
    "code": {
      "type": "integer",
      "description": ""
    },
    "message": {
      "type": "string",
      "description": ""
    },
    "data": {
      "type": "object",
      "properties": {
        "productId": {
          "type": "integer",
          "description": "",
          "format": "int64"
        },
        "productName": {
          "type": "string",
          "description": ""
        },
        "barcode": {
          "type": "string",
          "description": ""
        },
        "specification": {
          "type": "string",
          "description": ""
        },
        "categoryId": {
          "type": "integer",
          "description": ""
        },
        "brand": {
          "type": "string",
          "description": ""
        },
        "purchasePrice": {
          "type": "number",
          "description": ""
        },
        "sellingPrice": {
          "type": "number",
          "description": ""
        },
        "unit": {
          "type": "string",
          "description": ""
        },
        "stockQuantity": {
          "type": "integer",
          "description": ""
        },
        "minStock": {
          "type": "integer",
          "description": ""
        },
        "maxStock": {
          "type": "integer",
          "description": ""
        },
        "imageUrl": {
          "type": "string",
          "description": ""
        },
        "description": {
          "type": "string",
          "description": ""
        },
        "status": {
          "type": "integer",
          "description": ""
        },
        "createdAt": {
          "type": "string",
          "description": ""
        },
        "updatedAt": {
          "type": "string",
          "description": ""
        },
        "createdBy": {
          "type": "string",
          "description": ""
        },
        "supplierId": {
          "type": "integer",
          "description": ""
        }
      },
      "description": ""
    }
  }
}
```

**Schema 校验错误**：
- `$.data.specification: expected string`
- `$.data.categoryId: expected integer, got NoneType`
- `$.data.brand: expected string`
- `$.data.unit: expected string`
- `$.data.maxStock: expected integer, got NoneType`
- `$.data.imageUrl: expected string`
- `$.data.description: expected string`
- `$.data.createdBy: expected string`
- `$.data.supplierId: expected integer, got NoneType`

#### 数据库快照（products 表）

```json
[]
```

---

### 🐛 [Auto-Test] 更新状态 updateStatus - 200 (Schema不匹配)

**标签**：`bug` `auto-test`

**失败原因**：HTTP 状态码为 2xx，但响应不符合 JSON Schema

#### 请求 URL

`PATCH http://localhost:8080/api/products/25/status`

#### 请求参数

```json
{
  "id": 25,
  "status": 0
}
```

#### 实际响应报文

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "productId": 25,
    "productName": "接口更新商品-AutoTest",
    "barcode": "6900000000666",
    "specification": null,
    "categoryId": null,
    "brand": null,
    "purchasePrice": 11.0,
    "sellingPrice": 22.0,
    "unit": null,
    "stockQuantity": 33,
    "minStock": 0,
    "maxStock": null,
    "imageUrl": null,
    "description": null,
    "status": 0,
    "createdAt": "2026-07-07T19:46:07",
    "updatedAt": "2026-07-07T19:46:07.884",
    "createdBy": null,
    "supplierId": null
  }
}
```

#### 预期响应

- **HTTP 状态码**：200（2xx）
- **JSON Schema**：

```json
{
  "type": "object",
  "properties": {
    "code": {
      "type": "integer",
      "description": ""
    },
    "message": {
      "type": "string",
      "description": ""
    },
    "data": {
      "type": "object",
      "properties": {
        "productId": {
          "type": "integer",
          "description": "",
          "format": "int64"
        },
        "productName": {
          "type": "string",
          "description": ""
        },
        "barcode": {
          "type": "string",
          "description": ""
        },
        "specification": {
          "type": "string",
          "description": ""
        },
        "categoryId": {
          "type": "integer",
          "description": ""
        },
        "brand": {
          "type": "string",
          "description": ""
        },
        "purchasePrice": {
          "type": "number",
          "description": ""
        },
        "sellingPrice": {
          "type": "number",
          "description": ""
        },
        "unit": {
          "type": "string",
          "description": ""
        },
        "stockQuantity": {
          "type": "integer",
          "description": ""
        },
        "minStock": {
          "type": "integer",
          "description": ""
        },
        "maxStock": {
          "type": "integer",
          "description": ""
        },
        "imageUrl": {
          "type": "string",
          "description": ""
        },
        "description": {
          "type": "string",
          "description": ""
        },
        "status": {
          "type": "integer",
          "description": ""
        },
        "createdAt": {
          "type": "string",
          "description": ""
        },
        "updatedAt": {
          "type": "string",
          "description": ""
        },
        "createdBy": {
          "type": "string",
          "description": ""
        },
        "supplierId": {
          "type": "integer",
          "description": ""
        }
      },
      "description": ""
    }
  }
}
```

**Schema 校验错误**：
- `$.data.specification: expected string`
- `$.data.categoryId: expected integer, got NoneType`
- `$.data.brand: expected string`
- `$.data.unit: expected string`
- `$.data.maxStock: expected integer, got NoneType`
- `$.data.imageUrl: expected string`
- `$.data.description: expected string`
- `$.data.createdBy: expected string`
- `$.data.supplierId: expected integer, got NoneType`

#### 数据库快照（products 表）

```json
[]
```

---

### ✅ [Auto-Test] 删除商品 delete

- **标签**：`auto-test`
- **HTTP 状态码**：200
- **Schema 校验**：通过

## 数据库全局快照（测试结束时）

关联表：`products`

```json
[
  {
    "product_id": 4,
    "product_name": "apale",
    "barcode": "3",
    "status": 0,
    "stock_quantity": 0,
    "updated_at": "2026-05-26 11:29:44"
  },
  {
    "product_id": 5,
    "product_name": "apple1",
    "barcode": "4",
    "status": 0,
    "stock_quantity": 0,
    "updated_at": "2026-05-26 11:29:45"
  },
  {
    "product_id": 11,
    "product_name": "农夫山泉 550ml",
    "barcode": "6901122334455",
    "status": 1,
    "stock_quantity": 500,
    "updated_at": "2026-05-26 06:10:54"
  },
  {
    "product_id": 13,
    "product_name": "华为蓝牙耳机 FreeBuds 5",
    "barcode": "6903344556677",
    "status": 1,
    "stock_quantity": 25,
    "updated_at": "2026-05-26 06:10:54"
  },
  {
    "product_id": 14,
    "product_name": "得力固体胶 12g",
    "barcode": "6904455667788",
    "status": 1,
    "stock_quantity": 400,
    "updated_at": "2026-05-26 06:10:54"
  }
]
```

## 结论与建议

1. **delete** 接口测试通过，响应结构与 Schema 一致。
2. **list / create / getById / update / updateStatus** 均返回 2xx，但响应中存在大量 `null` 字段（如 `specification`、`categoryId`、`brand` 等），与 Apifox 文档中定义的 `string`/`integer` 类型不一致。
3. **list** 接口的分页字段 `sort` 实际为对象，文档定义为数组，存在结构差异。
4. **create** 实际返回 **201 Created**，文档定义为 **200**（状态码层面仍属 2xx，但文档需同步）。
5. 建议：在 OpenAPI Schema 中为可空字段添加 `nullable: true`，或 API 层对 null 字段返回空字符串/默认值。