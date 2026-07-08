# Push local main branch to GitHub repo qkxbdnkdbd/mcp
$ErrorActionPreference = "Stop"

$repoUrl = "https://github.com/qkxbdnkdbd/mcp.git"
$emptyConfig = Join-Path $env:TEMP "git-empty-config-mcp"
New-Item -ItemType File -Force -Path $emptyConfig | Out-Null
$env:GIT_CONFIG_GLOBAL = $emptyConfig
$env:GIT_CONFIG_SYSTEM = $emptyConfig

Set-Location (Split-Path $PSScriptRoot -Parent)

Write-Host "Checking remote repository..."
$check = git ls-remote $repoUrl 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Remote repository not found. Please create it first:"
    Write-Host "  https://github.com/new  -> name: mcp (empty, no README)"
    Write-Host ""
    exit 1
}

Write-Host "Pushing main branch..."
git push $repoUrl main
if ($LASTEXITCODE -eq 0) {
    git branch --set-upstream-to=origin/main main 2>$null
    git remote set-url origin $repoUrl
    Write-Host ""
    Write-Host "Success: https://github.com/qkxbdnkdbd/mcp"
} else {
    Write-Host "Push failed. Ensure GitHub credentials are configured (gh auth login)."
    exit 1
}
