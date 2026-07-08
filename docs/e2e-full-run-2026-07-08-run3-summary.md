# 全量 E2E + 接口测试汇总（Run 3）

**测试时间**：2026-07-08 16:04 UTC+8  
**策略**：UI 12 步全部执行（失败不中断）；8 个 API 全部执行

---

## UI E2E（12/12 已执行）

| 步骤 | 操作 | 结果 |
|------|------|------|
| UI-1~7 | 登录/列表/筛选/分页/刷新 | ✅ |
| UI-8 | 新增产品 | ✅（keyword 筛选 fallback） |
| UI-9 | 编辑产品 | ✅ |
| UI-10 | 切换状态 | ✅ |
| UI-11 | 删除产品 | ❌ Failed to fetch |
| UI-12 | 退出登录 | ❌ Delete 弹窗遮挡 |

---

## 全部接口测试

| 接口 | 结果 |
|------|------|
| POST /api/auth/login | ✅ |
| GET /api/products | ✅ |
| GET /api/products?keyword | ✅ |
| POST /api/products minimal | ❌ 500 |
| POST /api/products full | ✅ |
| GET /api/products/{id} | ✅ |
| PUT /api/products/{id} | ✅ |
| PATCH /api/products/{id}/status | ✅ |
| DELETE /api/products/{id} | ✅ |

---

## GitHub Issues

| Issue | 类型 | 状态 | 说明 |
|-------|------|------|------|
| [#1](https://github.com/qkxbdnkdbd/mcp/issues/1) | 【后端Bug】 | OPEN | minimal create 500，Run3 复测仍失败 |
| [#2](https://github.com/qkxbdnkdbd/mcp/issues/2) | 【前端Bug】 | OPEN | 创建后分页不可见，Run3 代码未修复 |
| [#3](https://github.com/qkxbdnkdbd/mcp/issues/3) | 【前端Bug】 | OPEN | 删除失败弹窗不关闭 |

---

## Run3 新增失败详情

### UI-11 删除

- URL: http://127.0.0.1:5174/
- 错误: Failed to fetch
- 网络: DELETE /api/products/45 => ERR_CONNECTION_REFUSED
- DB: 删除前 product_id=45 存在；后端恢复后 API DELETE 成功
- 归类: 【前端Bug】#3 — API 失败时 confirm 弹窗未关闭

### UI-12 登出（连带）

- Delete overlay 拦截 Logout 点击，归类同上 #3

### POST minimal（持续）

- 500 barcode cannot be null，归类 #1

---

**Apifox MCP**：不可用，使用 openapi.json 等价检查
