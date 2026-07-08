"""Destructive boundary tests for GET /api/products (list)."""

from __future__ import annotations

import json
import urllib.error
import urllib.parse
import urllib.request
from dataclasses import dataclass
from datetime import datetime
from pathlib import Path

BASE_URL = "http://localhost:8080/api/products"


@dataclass
class TestCase:
    case_id: int
    name: str
    category: str
    params: dict[str, str]
    expected: str


TEST_CASES = [
    TestCase(
        1,
        "status 传入非数字字符串",
        "类型错误",
        {"status": "abc"},
        "400 明确提示参数类型错误",
    ),
    TestCase(
        2,
        "categoryId 传入浮点数字符串",
        "类型错误",
        {"categoryId": "12.34"},
        "400 明确提示参数类型错误",
    ),
    TestCase(
        3,
        "supplierId 传入空字符串",
        "必填/空值边界",
        {"supplierId": ""},
        "400 或忽略空值返回 200",
    ),
    TestCase(
        4,
        "status 传入超大整数",
        "数值越界",
        {"status": "999999999999999999999"},
        "400 或 200（取决于框架解析）",
    ),
    TestCase(
        5,
        "status 传入负数",
        "数值越界",
        {"status": "-1"},
        "200 返回空列表或 400",
    ),
    TestCase(
        6,
        "keyword 超长字符串（10000 字符）",
        "字符串超长",
        {"keyword": "A" * 10000},
        "200 或 400，不应 500",
    ),
    TestCase(
        7,
        "keyword SQL 注入尝试",
        "SQL 注入",
        {"keyword": "' OR 1=1 --"},
        "200 返回过滤结果，不应 500",
    ),
    TestCase(
        8,
        "keyword 含非法/特殊字符",
        "非法字符",
        {"keyword": "<script>alert(1)</script>"},
        "200，不应 500",
    ),
    TestCase(
        9,
        "pageable size 为负数",
        "分页越界",
        {"size": "-1"},
        "400 明确提示分页参数非法",
    ),
    TestCase(
        10,
        "sort 注入恶意字段名",
        "SQL/排序注入",
        {"sort": "productId;DROP TABLE products--,desc"},
        "400 或 200，不应 500",
    ),
]


def run_case(test_case: TestCase) -> dict:
    query = urllib.parse.urlencode(test_case.params)
    url = f"{BASE_URL}?{query}"
    request = urllib.request.Request(url, method="GET")
    result = {
        "case_id": test_case.case_id,
        "name": test_case.name,
        "category": test_case.category,
        "params": test_case.params,
        "expected": test_case.expected,
        "url": url,
    }
    try:
        with urllib.request.urlopen(request, timeout=30) as response:
            body_text = response.read().decode("utf-8")
            try:
                body = json.loads(body_text)
            except json.JSONDecodeError:
                body = body_text
            result.update(
                {
                    "status": response.status,
                    "body": body,
                    "issue": classify_issue(response.status, body),
                }
            )
            return result
    except urllib.error.HTTPError as error:
        body_text = error.read().decode("utf-8")
        try:
            body = json.loads(body_text)
        except json.JSONDecodeError:
            body = body_text
        result.update(
            {
                "status": error.code,
                "body": body,
                "issue": classify_issue(error.code, body),
            }
        )
        return result
    except Exception as error:
        result.update({"status": 0, "body": str(error), "issue": "连接失败"})
        return result


def classify_issue(status: int, body) -> str | None:
    if status == 500:
        return "服务器 500 报错"
    if status == 400:
        if isinstance(body, dict) and body.get("message") not in (
            None,
            "validation failed",
        ):
            return None
        if isinstance(body, dict) and body.get("code") == 400:
            return None
        return "400 但错误信息不够明确"
    if status == 200:
        return None
    if status in (404, 405):
        return f"非预期状态码 {status}"
    return f"非预期状态码 {status}"


def write_report(results: list[dict], output_path: Path) -> None:
    now = datetime.now()
    bad_500 = [r for r in results if r.get("status") == 500]
    bad_400 = [r for r in results if r.get("issue") == "400 但错误信息不够明确"]
    unexpected = [r for r in results if r.get("issue") and r not in bad_500 and r not in bad_400]

    lines = [
        f"# {now.strftime('%Y-%m-%d %H:%M:%S')} ProductController list 破坏性边界测试报告",
        "",
        "> **接口**：`GET /api/products`（Apifox list）",
        "> **文档来源**：`openapi.json`",
        f"> **测试时间**：{now.strftime('%Y-%m-%d %H:%M:%S')}",
        "",
        "## Apifox 参数定义",
        "",
        "| 参数 | 类型 | 必填 | 说明 |",
        "|------|------|------|------|",
        "| keyword | string | 否 | 关键字搜索 |",
        "| status | integer | 否 | 状态筛选 |",
        "| categoryId | integer | 否 | 分类 ID |",
        "| supplierId | integer | 否 | 供应商 ID |",
        "",
        "另含 Spring 分页参数：`page`、`size`、`sort`（文档未显式声明）。",
        "",
        "## 测试摘要",
        "",
        f"| 用例总数 | {len(results)} |",
        f"| 触发 500 | {len(bad_500)} |",
        f"| 400 但不明确 | {len(bad_400)} |",
        f"| 其他异常 | {len(unexpected)} |",
        "",
        "## 用例设计与执行结果",
        "",
        "| # | 用例 | 类别 | HTTP | 问题 |",
        "|---|------|------|------|------|",
    ]

    for result in results:
        issue = result.get("issue") or "符合预期"
        lines.append(
            f"| {result['case_id']} | {result['name']} | {result['category']} | {result.get('status')} | {issue} |"
        )

    lines.extend(["", "## 详细结果", ""])

    for result in results:
        lines.extend(
            [
                f"### 用例 {result['case_id']}：{result['name']}",
                "",
                f"- **类别**：{result['category']}",
                f"- **预期**：{result['expected']}",
                f"- **请求 URL**：`{result['url']}`",
                f"- **HTTP 状态码**：{result.get('status')}",
                f"- **问题判定**：{result.get('issue') or '无'}",
                "",
                "**响应体**：",
                "",
                "```json",
                json.dumps(result.get("body"), ensure_ascii=False, indent=2)
                if isinstance(result.get("body"), (dict, list))
                else str(result.get("body")),
                "```",
                "",
            ]
        )

    lines.extend(["## 问题汇总", ""])

    if bad_500:
        lines.append("### 导致 500 的用例")
        lines.append("")
        for result in bad_500:
            lines.append(f"- **用例 {result['case_id']}** {result['name']}：`{result['url']}`")
        lines.append("")
    else:
        lines.append("### 导致 500 的用例")
        lines.append("")
        lines.append("- 无")
        lines.append("")

    if bad_400:
        lines.append("### 400 但提示不明确的用例")
        lines.append("")
        for result in bad_400:
            lines.append(f"- **用例 {result['case_id']}** {result['name']}")
        lines.append("")
    else:
        lines.append("### 400 但提示不明确的用例")
        lines.append("")
        lines.append("- 无")
        lines.append("")

    lines.extend(
        [
            "## 结论",
            "",
            "- list 接口所有查询参数在 Apifox 中均为**非必填**；破坏性测试主要覆盖类型错误、越界、注入与超长输入。",
            "- 若出现 500，通常是因为 Spring 参数绑定异常未被 `GlobalExceptionHandler` 专门处理，落入了通用 `Exception` 处理器。",
            "",
        ]
    )

    output_path.write_text("\n".join(lines), encoding="utf-8")


def main() -> None:
    results = [run_case(case) for case in TEST_CASES]
    filename = (
        f"{datetime.now().strftime('%Y-%m-%d_%H-%M-%S')}_list接口破坏性边界测试报告.md"
    )
    output_path = Path(__file__).resolve().parents[1] / "docs" / filename
    output_path.parent.mkdir(parents=True, exist_ok=True)
    write_report(results, output_path)
    print(json.dumps({"report": str(output_path), "results": results}, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
