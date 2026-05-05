## 1. 小说代理人资产落地

- [x] 1.1 在 `.cursor/skills/novel-writing-agent/` 创建 `SKILL.md`，摘要引用 `openspec/changes/novel-writing-agent/specs/novel-writing-agent/spec.md` 中的 MUST 条款，并写明触发关键词（小说、章节、续写、字数统计、极道天魔等）
- [x] 1.2 在 `SKILL.md` 中附上「章末字数统计」模板一行及 PowerShell 校验示例，与 spec 口径一致
- [x] 1.3 （可选）新增 `scripts/update-chapter-wordcount.ps1`，自动重写指定 md 文件末尾的 `（本章字数统计：…）` 行

## 2. 校验与对齐

- [x] 2.1 对照现有 `极道天魔/` 样章，确认 spec 与正文实践一致；若有偏差，更新 spec 或样章其一并记录理由
- [x] 2.2 在 `SKILL.md` 中补充 `@novel-writing-agent` 引用方式；本机「续写第 N 章」检索需用户在 IDE 中自测

## 3. 收尾

- [x] 3.1 运行 `openspec status --change novel-writing-agent`，确认 `tasks` 所依赖项已完成后，执行 `/opsx:apply` 勾选完成的任务或进入归档流程
