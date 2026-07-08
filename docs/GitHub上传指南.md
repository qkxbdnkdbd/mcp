# 推送到 GitHub 指南

本地代码已提交到 `main` 分支。由于当前 GitHub Token **没有创建仓库权限**，需要你在网页上先创建空仓库，再执行推送。

## 第一步：在 GitHub 创建仓库

1. 打开 [https://github.com/new](https://github.com/new)
2. **Repository name** 填写：`mcp`
3. 选择 **Public**
4. **不要**勾选 "Add a README file"（保持空仓库）
5. 点击 **Create repository**

## 第二步：推送代码

在项目根目录执行：

```powershell
powershell -ExecutionPolicy Bypass -File scripts/push-to-github.ps1
```

或手动推送（绕过 git 全局 `git://` 协议重写）：

```powershell
$env:GIT_CONFIG_GLOBAL = "$env:TEMP\git-empty-config"
New-Item -ItemType File -Force -Path $env:GIT_CONFIG_GLOBAL | Out-Null
git push https://github.com/qkxbdnkdbd/mcp.git main
```

## 可选：升级 Token 权限

若希望 Agent 能自动创建仓库，请在 GitHub 重新生成 Personal Access Token，勾选 **`repo`** 权限，并更新 Cursor MCP 配置中的 `GITHUB_PERSONAL_ACCESS_TOKEN`。

## 仓库地址

创建并推送成功后：

**https://github.com/qkxbdnkdbd/mcp**

## 安全说明

- 数据库密码、JWT 密钥已从 `application.yml` 中移除，改为环境变量注入
- 本地运行时请设置：`DB_URL`、`DB_USERNAME`、`DB_PASSWORD`、`JWT_SECRET`
