## Context

仓库中已有小说示例目录 `极道天魔/`，实践上形成了若干隐含规则：单章篇幅、题材（恐怖升级 + 主角爽向）、章末字数统计、简体中文输出等。这些规则此前仅存在于对话，缺少单一真源（single source of truth）。

## Goals / Non-Goals

**Goals:**

- 用 OpenSpec 的 `spec.md` 将「小说代理人」行为写成可检验的 SHALL/MUST 需求与场景。
- 指定落地方式：以 Cursor **Agent Skill**（`SKILL.md`）作为执行层，使代理在相关任务中默认遵循规格。
- 章末字数统计的口径写清楚，减少手工与自动统计不一致。

**Non-Goals:**

- 不规定具体剧情、人物名或世界观细节（仅规定写作代理的约束与交付格式）。
- 不在本变更中实现商业出版流程（签约、排版、ISBN 等）。
- 不强制引入特定 LLM 或外部写作 SaaS。

## Decisions

1. **规格名称与展示名**  
   - **决定**：能力目录使用 `novel-writing-agent`（kebab-case）；对人说明时使用中文名「小说代理人」。  
   - **理由**：符合 OpenSpec 与文件系统约定；中文名便于团队沟通。

2. **落地载体：Skill 优先于零散 RULE**  
   - **决定**：在 `.cursor/skills/novel-writing-agent/SKILL.md` 中编写技能，前置条件写明「创作/续写 `极道天魔` 或用户指定小说目录时必读」。  
   - **备选**：仅写 `AGENTS.md` 或 `.cursor/rules` —— 覆盖面不如 Skill 在「写小说」任务上可发现性强。  
   - **理由**：用户已使用 Cursor Skills；小说任务与代码任务解耦，Skill 更合适。

3. **字数统计口径**  
   - **决定**：默认与仓库实践一致——以整章 Markdown 源文件的 `Get-Content -Raw` 字符串长度（.NET `Length`）为「含标题、标点、空格与换行」的字符数；**章末统计行本身不计入**该行所报告的数字。  
   - **理由**：与用户已确认的 Windows/PowerShell 环境一致；在 spec 中用 MUST 写清，避免歧义。

4. **原创与版权**  
   - **决定**：技能与 spec 均要求代理人产出为**独立原创**，不得复述或拼接知名作品的独创表达；同名标题仅可作为用户文件夹名，不视为授权仿写。  
   - **理由**：降低法律与平台风险。

5. **编辑范围默认「单章点名」**  
   - **决定**：每次修改默认仅允许动用户**明确点名**的章节；未点名则不碰其他文件，未指明章节则先问清。  
   - **理由**：避免「顺手改全书」造成用户失控与 diff 噪声；与用户工作流一致。

## Risks / Trade-offs

- **[Risk] 字数口径与「纯汉字」平台统计不一致** → **缓解**：在 spec 中允许用户声明「平台统计」为变体口径，并说明默认口径；技能中提示投稿前按平台重算。  
- **[Risk] Skill 未被自动触发** → **缓解**：在技能 description 中覆盖「小说、章节、续写、极道天魔、字数统计」等关键词；tasks 中含手动验证项。  
- **[Trade-off] 规格偏长** → **缓解**：Skill 正文保持 checklist 化，详细论述留在 `spec.md` 归档。

## Migration Plan

- 变更归档后：将 `specs/novel-writing-agent/spec.md` 同步到 `openspec/specs/`（由 archive 流程处理）；技能文件随仓库提交即可，无部署步骤。

## Open Questions

- `scripts/update-chapter-wordcount.ps1` 已提供：仅接受单文件 `-Path`，与「点名章节」策略一致；若需批量，必须显式循环或单独变更授权。
