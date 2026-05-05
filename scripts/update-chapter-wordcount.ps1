<#!
  按「小说代理人」口径更新单个章节文件末尾的：
  （本章字数统计：NNNN 字）

  统计值 = 去掉该行之后，剩余全文字符串的 .NET Length（含标点、换行、Markdown）。
  仅修改 -Path 指定的单个文件；请显式传入路径，避免误批量。
#>
param(
    [Parameter(Mandatory = $true, Position = 0)]
    [string] $Path
)

$resolved = Resolve-Path -LiteralPath $Path -ErrorAction Stop
$raw = Get-Content -LiteralPath $resolved -Raw -ErrorAction Stop
if ($null -eq $raw) {
    $raw = ""
}

$pattern = '(?:\r?\n)（本章字数统计：\d+\s*字）\s*$'
$body = [regex]::Replace($raw, $pattern, "", [System.Text.RegularExpressions.RegexOptions]::None)

# 若文件此前没有统计行，则整篇参与计数（符合 spec：写入统计行前全长）
$count = $body.Length
$newLine = "（本章字数统计：$count 字）"

if ([regex]::IsMatch($raw, $pattern)) {
    $out = [regex]::Replace($raw, $pattern, "`r`n$newLine", [System.Text.RegularExpressions.RegexOptions]::None)
} else {
    $trimmed = $body.TrimEnd()
    $out = $trimmed + "`r`n`r`n$newLine`r`n"
}

[System.IO.File]::WriteAllText($resolved, $out, [System.Text.UTF8Encoding]::new($false))

Write-Host "Updated: $resolved"
Write-Host "Count (excluding stats line): $count"
