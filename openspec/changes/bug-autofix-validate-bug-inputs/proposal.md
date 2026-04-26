## Why

当前 `bug-autofix-loop` 更偏向“已知失败（build/test/lint/crash 输出）→ 诊断修复”的闭环，但在真实使用中经常出现“我怀疑有 bug / 我有一串缺陷清单”的输入形态。需要把“验证 bug 是否存在、给出可复现步骤”的阶段显式纳入流程，避免直接进入修复导致误修或无法验收。

## What Changes

- 支持两种输入：**Bug 清单**（多条）与**自然语言描述**（单条问题）
- 在修复前新增“存在性验证/可复现性验证”阶段：输出标准化复现流程（环境、步骤、预期/实际、证据）
- 对不可复现/证据不足的情况规定回退行为：请求补充信息、最小复现工程/日志、或将问题标记为环境/不稳定（flaky）
- 将“复现步骤 + 验证命令”作为修复方案的一部分，用于验收与回归验证

## Capabilities

### New Capabilities

- `bug-triage-and-repro`: 定义从“Bug 清单/自然语言问题”到“可复现步骤与存在性结论”的标准流程，并与后续最小修复与验证闭环衔接

### Modified Capabilities

<!-- 当前无 openspec/specs，先留空 -->

## Impact

- `.cursor/skills/bug-autofix-loop/SKILL.md`：需要新增/调整一个“Bug 输入 → 分诊 → 复现验证”的分支与输出模板
- `.cursor/skills/bug-autofix-loop/examples.md`：增加“Bug 清单驱动”的示例（含不可复现的回退示例）
- 使用体验：用户可以直接丢缺陷清单，skill 会先逐条产出复现与结论，再进入修复
