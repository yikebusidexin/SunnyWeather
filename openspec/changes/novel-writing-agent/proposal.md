## Why

在仓库内创作长篇小说时，规则分散在对话与既有习惯中，导致篇幅、爽点取向、字数统计与原创性约束不一致。需要把「小说代理人」的写作规范固化为可复用的变更与规格，便于后续用 `/opsx:apply` 落地为技能或流程文件。

## What Changes

- 新增 OpenSpec 能力 **`novel-writing-agent`**：定义「小说代理人」在篇幅、题材张力、主角取向、章节尾部字数统计、文件组织、**仅编辑用户点名章节**与原创性上的强制要求。
- 在 `design.md` 中约定落地载体（例如 Cursor Agent Skill 与章节目录约定），避免只停留在文档层。
- 提供可执行的 `tasks.md` 清单，用于把规格同步到仓库内的代理人资产（技能/说明）。

## Capabilities

### New Capabilities

- `novel-writing-agent`：约束与场景化要求，覆盖恐怖升级题材下主角「开挂爽文」取向、单章最低字数、章末字数统计行口径、简体中文正文与原创声明边界。

### Modified Capabilities

- （无；`openspec/specs/` 下当前无既有能力。）

## Impact

- **文档与流程**：`openspec/changes/novel-writing-agent/` 下的 proposal、design、specs、tasks。
- **后续代码/资产**：可能新增 `.cursor/skills/` 下的技能文件或更新 `极道天魔/` 目录约定说明；不改变 Android 应用代码。
- **依赖**：无新增运行时依赖；可选后续增加本地脚本用于自动更新章末字数。
