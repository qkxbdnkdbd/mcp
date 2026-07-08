# MCP 项目

Apifox API 文档、自动化测试脚本与 Products CRUD 后端。

## 目录结构

- `products-crud/` — Spring Boot 商品管理 API
- `docs/` — Apifox 接口文档与测试报告
- `scripts/` — 自动化测试脚本
- `openapi.json` / `openapi.yaml` — Apifox OpenAPI 规范

## 本地运行

```bash
cd products-crud
# 配置环境变量 DB_URL / DB_USERNAME / DB_PASSWORD / JWT_SECRET
mvn spring-boot:run
```

## 环境变量

| 变量 | 说明 |
|------|------|
| `DB_URL` | MySQL 连接 URL |
| `DB_USERNAME` | 数据库用户名 |
| `DB_PASSWORD` | 数据库密码 |
| `JWT_SECRET` | JWT 签名密钥 |
