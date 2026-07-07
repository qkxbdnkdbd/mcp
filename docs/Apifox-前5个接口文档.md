# Apifox 项目前 5 个接口文档

> **数据来源**：Apifox 项目 OpenAPI 规范（本地 MCP 缓存）  
> **项目 ID**：6470092  
> **项目名称**：默认模块  
> **项目描述**：likeadmin 是一套使用流行的技术栈的快速开发管理后台  
> **版本**：v1.7.0  
> **文档生成时间**：2026-07-07  
> **缓存更新时间**：2026-07-07T10:55:51.037Z

---

## 项目概览

| 字段 | 值 |
|------|-----|
| 联系人 | FZR |
| 联系邮箱 | tinyants@163.com |
| 项目地址 | https://gitee.com/likeadmin/likeadmin_java |
| 接口分组（前 5 个） | 代理商概况 |

---

## 接口列表

| # | 方法 | 路径 | 接口名称 | 分组 |
|---|------|------|----------|------|
| 1 | GET | `/front-api/api/front/agent/overview/activity-detail/{id}` | 代理商活动购买详情 | 代理商概况 |
| 2 | GET | `/front-api/api/front/agent/overview/my-inventory` | 查询我的库存商品列表 | 代理商概况 |
| 3 | GET | `/front-api/api/front/agent/overview/participated/detail/{participationId}` | 代理商参与的活动详情 | 代理商概况 |
| 4 | GET | `/front-api/front/api/agent/overview/records` | 获取代理商概况 | 代理商概况 |
| 5 | GET | `/front-api/api/front/agent/overview/purchase/available-inventory` | 代理商采购可用库存列表 | 代理商概况 |

---

## 接口详情

### 1. 代理商活动购买详情

- **Operation ID**：`getActivityPurchaseDetailUsingGET`
- **描述**：根据购买记录 ID 查询完整的活动购买详情

**路径参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | integer (int64) | 是 | 购买记录 ID |

**请求头（公共）**

| Header | 说明 |
|--------|------|
| wacan-admin | 管理端鉴权 Token |
| wacan-token | 用户 Token |
| tenant-id | 租户 ID |
| wacan-merchant | 商户鉴权 Token |
| merchant-id | 商户 ID |
| user-type | 用户类型，如 `merchant-api` |

---

### 2. 查询我的库存商品列表

- **Operation ID**：`getMyInventoryUsingGET`
- **描述**：支持按活动 ID、分类 ID、子分类 ID 等条件筛选

**查询参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityId | integer (int64) | 是 | 活动 ID |
| activityParticipationId | integer (int64) | 是 | 活动参与 ID |
| categoryId | integer (int64) | 否 | 分类 ID |
| keyword | string | 否 | 关键字搜索 |
| offset | integer (int32) | 否 | 分页偏移量 |

**请求头**：同接口 1

---

### 3. 代理商参与的活动详情

- **Operation ID**：`getParticipatedActivityDetailsUsingGET`

**路径参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| participationId | integer (int64) | 是 | 活动参与 ID |

**请求头**：同接口 1

---

### 4. 获取代理商概况

- **Operation ID**：`getAgentOverviewUsingGET`
- **描述**：获取当前代理商的概况数据

**请求头**：同接口 1

---

### 5. 代理商采购可用库存列表

- **Operation ID**：`getAvailableInventoryForPurchaseUsingGET`
- **描述**：根据活动参与 ID 查询可采购的商品列表

**查询参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityId | integer (int64) | 是 | 活动 ID |
| activityParticipationId | integer (int64) | 是 | 活动参与 ID |
| categoryId | integer (int64) | 否 | 分类 ID |
| keyword | string | 否 | 关键字搜索 |

**请求头**：同接口 1

---

## 备注

- 本文档基于 Apifox MCP Server 本地缓存的 OpenAPI 规范生成，按 `paths` 字段中的定义顺序取前 5 个接口。
- 若 Apifox 项目内有更新，请刷新 MCP 缓存后重新生成文档。
- 所有接口均属于 **代理商概况** 模块，请求时需携带相应的鉴权 Header。
