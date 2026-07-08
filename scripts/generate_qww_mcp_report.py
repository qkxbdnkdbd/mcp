"""Generate qww-mcp.md from test results JSON."""

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RESULTS_PATH = ROOT / "qww-test-results.json"
DB_SNAPSHOT_PATH = ROOT / "qww-db-snapshot.json"
OUTPUT_PATH = ROOT / "qww-mcp.md"

NAME_MAP = {
    "list": "商品列表 list",
    "create": "创建商品 create",
    "getById": "商品详情 getById",
    "update": "更新商品 update",
    "updateStatus": "更新状态 updateStatus",
    "delete": "删除商品 delete",
}


def main() -> None:
    with RESULTS_PATH.open(encoding="utf-8") as file:
        data = json.load(file)
    with DB_SNAPSHOT_PATH.open(encoding="utf-8") as file:
        db = json.load(file)

    lines: list[str] = []
    lines.append("# qww-test API 自动化测试报告")
    lines.append("")
    lines.append("> **模块**：qww-test（Products CRUD）")
    lines.append("> **数据源**：Apifox OpenAPI（`openapi.json`）")
    lines.append("> **Base URL**：`http://localhost:8080`")
    lines.append(f"> **测试时间**：{data['time']}")
    lines.append("")

    passed = sum(1 for test in data["tests"] if test["passed"])
    failed = len(data["tests"]) - passed

    lines.append("## 测试摘要")
    lines.append("")
    lines.append("| 指标 | 数值 |")
    lines.append("|------|------|")
    lines.append(f"| 接口总数 | {len(data['tests'])} |")
    lines.append(f"| 通过 | {passed} |")
    lines.append(f"| 失败 | {failed} |")
    lines.append("")
    lines.append("## 接口清单")
    lines.append("")
    lines.append("| # | 接口名称 | 方法 | 路径 | 结果 |")
    lines.append("|---|----------|------|------|------|")
    for index, test in enumerate(data["tests"], 1):
        result = "✅ 通过" if test["passed"] else "❌ 失败"
        display_name = NAME_MAP.get(test["name"], test["name"])
        lines.append(
            f"| {index} | {display_name} | {test['method']} | `{test['path']}` | {result} |"
        )
    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## 详细测试结果")
    lines.append("")

    for test in data["tests"]:
        display_name = NAME_MAP.get(test["name"], test["name"])
        if test["passed"]:
            lines.append(f"### ✅ [Auto-Test] {display_name}")
            lines.append("")
            lines.append("- **标签**：`auto-test`")
            lines.append(f"- **HTTP 状态码**：{test['status']}")
            lines.append("- **Schema 校验**：通过")
            lines.append("")
            continue

        if test["status"] < 200 or test["status"] >= 300:
            title_suffix = str(test["status"])
            fail_reason = f"HTTP 状态码 {test['status']} 非 2xx"
        else:
            title_suffix = f"{test['status']} (Schema不匹配)"
            fail_reason = "HTTP 状态码为 2xx，但响应不符合 JSON Schema"

        lines.append(f"### 🐛 [Auto-Test] {display_name} - {title_suffix}")
        lines.append("")
        lines.append("**标签**：`bug` `auto-test`")
        lines.append("")
        lines.append(f"**失败原因**：{fail_reason}")
        lines.append("")
        lines.append("#### 请求 URL")
        lines.append("")
        lines.append(f"`{test['method']} http://localhost:8080{test['path']}`")
        lines.append("")
        lines.append("#### 请求参数")
        lines.append("")
        lines.append("```json")
        lines.append(json.dumps(test["params"], ensure_ascii=False, indent=2))
        lines.append("```")
        lines.append("")
        lines.append("#### 实际响应报文")
        lines.append("")
        lines.append("```json")
        if test["body"] is not None:
            lines.append(json.dumps(test["body"], ensure_ascii=False, indent=2))
        else:
            lines.append(test["raw"])
        lines.append("```")
        lines.append("")
        lines.append("#### 预期响应")
        lines.append("")
        lines.append(f"- **HTTP 状态码**：{test['expected_status']}（2xx）")
        lines.append("- **JSON Schema**：")
        lines.append("")
        lines.append("```json")
        lines.append(json.dumps(test["expected_schema"], ensure_ascii=False, indent=2))
        lines.append("```")
        if test.get("schema_errors"):
            lines.append("")
            lines.append("**Schema 校验错误**：")
            for error in test["schema_errors"]:
                lines.append(f"- `{error}`")
        lines.append("")
        lines.append("#### 数据库快照（products 表）")
        lines.append("")
        related_id = test["params"].get("id")
        if test["name"] == "list":
            snapshot = db
        elif related_id:
            snapshot = [row for row in db if row["product_id"] == related_id]
        else:
            snapshot = db
        lines.append("```json")
        lines.append(json.dumps(snapshot, ensure_ascii=False, indent=2))
        lines.append("```")
        lines.append("")
        lines.append("---")
        lines.append("")

    lines.append("## 数据库全局快照（测试结束时）")
    lines.append("")
    lines.append("关联表：`products`")
    lines.append("")
    lines.append("```json")
    lines.append(json.dumps(db, ensure_ascii=False, indent=2))
    lines.append("```")
    lines.append("")
    lines.append("## 结论与建议")
    lines.append("")
    lines.append("1. **delete** 接口测试通过，响应结构与 Schema 一致。")
    lines.append(
        "2. **list / create / getById / update / updateStatus** 均返回 2xx，"
        "但响应中存在大量 `null` 字段（如 `specification`、`categoryId`、`brand` 等），"
        "与 Apifox 文档中定义的 `string`/`integer` 类型不一致。"
    )
    lines.append(
        "3. **list** 接口的分页字段 `sort` 实际为对象，文档定义为数组，存在结构差异。"
    )
    lines.append(
        "4. **create** 实际返回 **201 Created**，文档定义为 **200**（状态码层面仍属 2xx，但文档需同步）。"
    )
    lines.append(
        "5. 建议：在 OpenAPI Schema 中为可空字段添加 `nullable: true`，"
        "或 API 层对 null 字段返回空字符串/默认值。"
    )

    OUTPUT_PATH.write_text("\n".join(lines), encoding="utf-8")
    print(f"Report written to {OUTPUT_PATH}")


if __name__ == "__main__":
    main()
