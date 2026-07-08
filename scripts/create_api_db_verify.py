"""Call create API with random data and verify DB insert."""

from __future__ import annotations

import json
import random
import string
import urllib.error
import urllib.request
from datetime import datetime
from decimal import Decimal
from pathlib import Path

import pymysql

BASE_URL = "http://localhost:8080"
DB_CONFIG = {
    "host": "dbconn.sealoshzh.site",
    "port": 39811,
    "user": "root",
    "password": "qww123456",
    "database": "qww-product",
    "connect_timeout": 15,
}

CORE_FIELDS = [
    "product_name",
    "barcode",
    "specification",
    "category_id",
    "brand",
    "purchase_price",
    "selling_price",
    "unit",
    "stock_quantity",
    "min_stock",
    "max_stock",
    "image_url",
    "description",
    "status",
    "created_by",
    "supplier_id",
]

AUTO_FIELDS = ["product_id", "created_at", "updated_at"]

REQUEST_TO_DB = {
    "productName": "product_name",
    "barcode": "barcode",
    "specification": "specification",
    "categoryId": "category_id",
    "brand": "brand",
    "purchasePrice": "purchase_price",
    "sellingPrice": "selling_price",
    "unit": "unit",
    "stockQuantity": "stock_quantity",
    "minStock": "min_stock",
    "maxStock": "max_stock",
    "imageUrl": "image_url",
    "description": "description",
    "status": "status",
    "createdBy": "created_by",
    "supplierId": "supplier_id",
}


def random_suffix(length: int = 8) -> str:
    return "".join(random.choices(string.ascii_lowercase + string.digits, k=length))


def build_random_payload() -> dict:
    suffix = random_suffix()
    return {
        "productName": f"Apifox随机测试商品-{suffix}",
        "barcode": f"690{random.randint(1000000000, 9999999999)}",
        "specification": f"规格-{suffix}",
        "categoryId": random.randint(1, 20),
        "brand": f"品牌-{suffix[:4]}",
        "purchasePrice": round(random.uniform(10.0, 500.0), 2),
        "sellingPrice": round(random.uniform(20.0, 800.0), 2),
        "unit": random.choice(["件", "箱", "瓶", "台"]),
        "stockQuantity": random.randint(1, 999),
        "minStock": random.randint(1, 50),
        "maxStock": random.randint(100, 2000),
        "imageUrl": f"https://example.com/images/{suffix}.png",
        "description": f"Apifox create 接口随机测试数据，批次 {suffix}",
        "status": random.choice([0, 1]),
        "supplierId": random.randint(1, 100),
    }


def call_create(payload: dict) -> tuple[int, dict | None, str]:
    data = json.dumps(payload, ensure_ascii=False).encode("utf-8")
    request = urllib.request.Request(
        f"{BASE_URL}/api/products",
        data=data,
        headers={"Content-Type": "application/json; charset=utf-8"},
        method="POST",
    )
    try:
        with urllib.request.urlopen(request, timeout=30) as response:
            body = response.read().decode("utf-8")
            return response.status, json.loads(body), body
    except urllib.error.HTTPError as error:
        body = error.read().decode("utf-8")
        try:
            return error.code, json.loads(body), body
        except json.JSONDecodeError:
            return error.code, None, body


def normalize(value):
    if value is None:
        return None
    if isinstance(value, Decimal):
        return float(value)
    if isinstance(value, datetime):
        return value.replace(microsecond=0)
    if isinstance(value, str) and value.endswith("Z"):
        return value[:-1]
    return value


def compare_core(request_payload: dict, db_row: dict) -> list[dict]:
    rows = []
    for req_key, db_key in REQUEST_TO_DB.items():
        expected = request_payload.get(req_key)
        actual = db_row.get(db_key)
        if req_key in {"purchasePrice", "sellingPrice"}:
            expected = float(expected) if expected is not None else 0.0
            actual = float(actual) if actual is not None else 0.0
        elif req_key in {"stockQuantity", "minStock"}:
            expected = expected if expected is not None else 0
            actual = actual if actual is not None else 0
        elif req_key == "status":
            expected = expected if expected is not None else 1
        elif req_key == "createdBy":
            expected = None
        matched = normalize(expected) == normalize(actual)
        rows.append(
            {
                "field": db_key,
                "request_key": req_key,
                "expected": expected,
                "actual": actual,
                "matched": matched,
            }
        )
    return rows


def validate_auto_fields(api_data: dict, db_row: dict, test_started_at: datetime) -> list[dict]:
    checks = []

    product_id = db_row.get("product_id")
    checks.append(
        {
            "field": "product_id",
            "expected": "数据库自增主键，且与接口返回 productId 一致",
            "actual": product_id,
            "matched": product_id is not None
            and product_id > 0
            and product_id == api_data.get("productId"),
            "detail": f"API productId={api_data.get('productId')}, DB product_id={product_id}",
        }
    )

    created_at = db_row.get("created_at")
    updated_at = db_row.get("updated_at")
    checks.append(
        {
            "field": "created_at",
            "expected": "创建时自动写入当前时间，且与 updated_at 基本一致",
            "actual": str(created_at),
            "matched": created_at is not None
            and updated_at is not None
            and abs((created_at - updated_at).total_seconds()) <= 1,
            "detail": f"created_at={created_at}, updated_at={updated_at}",
        }
    )

    checks.append(
        {
            "field": "updated_at",
            "expected": "创建时自动写入当前时间，应接近测试执行时间",
            "actual": str(updated_at),
            "matched": updated_at is not None
            and abs((updated_at - test_started_at).total_seconds()) <= 30,
            "detail": f"测试开始时间={test_started_at}, updated_at={updated_at}",
        }
    )

    return checks


def fetch_db_row(product_id: int) -> dict:
    conn = pymysql.connect(**DB_CONFIG)
    try:
        with conn.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute("SELECT * FROM products WHERE product_id = %s", (product_id,))
            row = cursor.fetchone()
            if row is None:
                raise RuntimeError(f"数据库未找到 product_id={product_id}")
            return row
    finally:
        conn.close()


def write_report(
    output_path: Path,
    test_time: datetime,
    payload: dict,
    status_code: int,
    api_body: dict | None,
    db_row: dict,
    core_checks: list[dict],
    auto_checks: list[dict],
) -> None:
    core_pass = all(item["matched"] for item in core_checks)
    auto_pass = all(item["matched"] for item in auto_checks)
    overall = core_pass and auto_pass and status_code in (200, 201)

    lines = [
        f"# {test_time.strftime('%Y-%m-%d %H:%M:%S')} create 接口入参与数据库一致性校验报告",
        "",
        "> **接口来源**：Apifox / OpenAPI `POST /api/products`（create）",
        f"> **测试时间**：{test_time.strftime('%Y-%m-%d %H:%M:%S')}",
        f"> **Base URL**：`{BASE_URL}`",
        "",
        "## 测试结论",
        "",
        f"- **接口调用**：{'成功' if status_code in (200, 201) else '失败'}（HTTP {status_code}）",
        f"- **核心业务字段校验**：{'全部一致' if core_pass else '存在不一致'}",
        f"- **自动生成字段校验**：{'符合预期' if auto_pass else '存在异常'}",
        f"- **总体结果**：{'✅ 通过' if overall else '❌ 未通过'}",
        "",
        "## 1. 接口请求",
        "",
        "### 请求 URL",
        "",
        f"`POST {BASE_URL}/api/products`",
        "",
        "### 随机入参",
        "",
        "```json",
        json.dumps(payload, ensure_ascii=False, indent=2),
        "```",
        "",
        "## 2. 接口响应",
        "",
        "```json",
        json.dumps(api_body, ensure_ascii=False, indent=2),
        "```",
        "",
        "## 3. 数据库查询结果",
        "",
        f"查询 SQL：`SELECT * FROM products WHERE product_id = {db_row['product_id']}`",
        "",
        "```json",
        json.dumps(
            {key: (str(value) if isinstance(value, datetime) else value) for key, value in db_row.items()},
            ensure_ascii=False,
            indent=2,
            default=str,
        ),
        "```",
        "",
        "## 4. 核心业务字段对比",
        "",
        "| 数据库字段 | 请求字段 | 入参值 | 数据库值 | 是否一致 |",
        "|-----------|---------|--------|---------|---------|",
    ]

    for item in core_checks:
        mark = "✅" if item["matched"] else "❌"
        lines.append(
            f"| `{item['field']}` | `{item['request_key']}` | `{item['expected']}` | `{item['actual']}` | {mark} |"
        )

    lines.extend(
        [
            "",
            "## 5. 自动生成字段校验",
            "",
            "| 字段 | 预期逻辑 | 实际值 | 是否一致 | 说明 |",
            "|------|---------|--------|---------|------|",
        ]
    )

    for item in auto_checks:
        mark = "✅" if item["matched"] else "❌"
        lines.append(
            f"| `{item['field']}` | {item['expected']} | `{item['actual']}` | {mark} | {item['detail']} |"
        )

    lines.extend(
        [
            "",
            "## 6. 校验说明",
            "",
            "- **核心业务字段**：指 create 入参中直接映射到 `products` 表的业务列。",
            "- **自动生成字段**：",
            "  - `product_id`：MySQL 自增主键；",
            "  - `created_at` / `updated_at`：服务层在创建时写入当前时间。",
            "- **默认值规则（来自 ProductService）**：",
            "  - `purchasePrice` / `sellingPrice` 为空时写入 `0`；",
            "  - `stockQuantity` / `minStock` 为空时写入 `0`；",
            "  - `status` 为空时默认 `1`。",
            "- **已知限制**：数据库 `created_by` 字段类型为 `int`，而 Apifox/API 定义为 `string`，传入字符串会导致 500，本次随机入参未包含 `createdBy`。",
            "",
        ]
    )

    output_path.write_text("\n".join(lines), encoding="utf-8")


def main() -> None:
    test_time = datetime.now()
    payload = build_random_payload()
    status_code, api_body, _ = call_create(payload)

    if status_code not in (200, 201) or not api_body or api_body.get("code") != 0:
        raise RuntimeError(f"create 接口调用失败: status={status_code}, body={api_body}")

    product_id = api_body["data"]["productId"]
    db_row = fetch_db_row(product_id)
    core_checks = compare_core(payload, db_row)
    auto_checks = validate_auto_fields(api_body["data"], db_row, test_time)

    filename = f"{test_time.strftime('%Y-%m-%d_%H-%M-%S')}_create接口入参与数据库一致性校验报告.md"
    output_path = Path(__file__).resolve().parents[1] / filename
    write_report(output_path, test_time, payload, status_code, api_body, db_row, core_checks, auto_checks)
    print(str(output_path))


if __name__ == "__main__":
    main()
