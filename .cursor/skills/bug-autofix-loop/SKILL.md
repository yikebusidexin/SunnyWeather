---
name: bug-autofix-loop
description: Diagnose and fix build/test/lint/crash issues in a repeatable loop (evidence → classify → propose → apply → verify). Use when the user asks to auto-fix bugs/defects, fix CI, fix build failures, fix test failures, fix lint issues, or fix crashes.
---

# Bug Auto-Fix Loop

## Quick Start

Use this workflow whenever the user asks for any of:
- 自动修复 bug / 缺陷 / defect
- 修 CI / 修 pipeline
- 修构建失败 / build failed / Gradle failed
- 修测试失败 / tests failed
- 修 lint / 静态检查失败
- 修崩溃 / crash / NPE / stacktrace

## 输入协议（两类入口）

你会遇到两种典型输入形态：

### A) 已有失败证据（推荐直接进入闭环）

例如用户已经提供了：
- 具体命令（CI step / `./gradlew ...`）与失败输出
- failing test 名 + diff/stacktrace
- lint 规则 id + file/line
- crash 的完整堆栈 + 触发步骤

这种情况直接走下方的 **Core Workflow**。

### B) Bug 清单 / 自然语言描述（先 triage，再决定是否修复）

例如用户只给了“现象描述”、或者一次性丢了多条缺陷清单，但没有可执行证据。

此时不要盲修：先进入 **Triage & Repro（修复前置）**，为每条 bug 输出“是否存在/是否可复现”的结论与复现材料，再决定是否进入修复闭环。

建议用户提供的最小字段（不强制）：
- **标题/编号**：例如 `#1 搜索页闪退`
- **环境**：设备/系统/版本/构建变体/账号（如相关）
- **证据**：命令输出/日志/堆栈/截图（如有）
- **优先级**：P0/P1/P2（可选）

## 存在性结论状态（每条 bug 必须给一个）

对每条 bug，输出下列之一作为状态（并说明原因与下一步）：
- `reproducible`：可复现（附复现报告与最小验证命令）
- `not-reproducible`：按当前信息不可复现（列出缺失信息/分歧点）
- `needs-info`：证据不足（给出最小缺失信息与获取方式）
- `flaky-suspected`：疑似不稳定（给出重跑/隔离策略与退出条件）
- `environment-blocked`：被环境阻塞（给出环境修复步骤与验证命令）

## 复现报告模板（可复制粘贴）

当你判定为 `reproducible`（或用户要求继续推进时），输出如下模板（尽量短，但字段要齐）：

- **环境**：<设备/系统/版本/构建变体/commit 或版本号>
- **步骤**：
  1. ...
  2. ...
- **预期**：...
- **实际**：...
- **证据**：<关键日志片段/堆栈首个根因/截图说明>
- **最小验证命令**：<一条命令或最小集合，例如 `./gradlew test...` / `./gradlew lint...`>

## Triage & Repro（修复前置，用于 bug 清单/自然语言输入）

当输入为“bug 清单”或“无证据的自然语言描述”时，按下述流程处理（默认先 triage，再修复）：

### T0) 先摘要再展开（避免信息淹没）

如果是多条 bug 清单，先输出一个摘要：
- 按类型分组：build / tests / lint / crash / unknown
- 给出建议顺序：先处理 `environment-blocked`（否则后续都跑不起来），再处理 `reproducible`，最后处理 `needs-info`/`flaky-suspected`

### T1) 逐条 triage（每条都必须落到一个状态）

对每条 bug：
- 提取最小可执行目标：复现步骤或最小验证命令（如果缺失就进入 `needs-info`）
- 尝试从描述中判断类型（build/tests/lint/crash/unknown）
- 输出 **状态** + **下一步**（复现报告或缺失信息清单）

### T2) 进入修复的闸门（默认需要可复现）

默认规则：
- 只有当状态为 `reproducible` 才进入修复闭环（下方 Core Workflow）
- 若用户明确要求“即使不可复现也先修”，必须输出：
  - 风险说明（可能误修/无法验收）
  - 关键假设（你基于什么推断）
  - 未来验收策略（用什么命令/步骤验证）

### T3) 回退分支（必须停止盲修）

- `needs-info` / `not-reproducible`：输出“最小缺失信息 + 获取命令”，并停止盲修，等待用户补齐
- `environment-blocked`：给出环境修复步骤与验证命令（例如 JDK/SDK、网络/镜像），并等待用户执行后再继续
- `flaky-suspected`：给出重跑与隔离策略；没有稳定复现前不要叠加多次“猜测性修复”

## Core Workflow (repeat until green or blocked)

### 0) Establish “definition of done”

Pick verification commands based on failure type (defaults for Android/Gradle projects):
- **build**: `./gradlew assembleDebug`
- **tests**: `./gradlew testDebugUnitTest` (or the failing test task reported by Gradle)
- **lint**: `./gradlew lintDebug`
- **crash**: reproduce + add/adjust a test when possible; at minimum ensure **build + relevant tests** pass

If the repo defines canonical commands (README/CI config), prefer those.

### 1) Evidence first (no blind fixes)

Before proposing changes, collect the smallest evidence set needed to be confident:
- The exact **repro command** (or the CI step command)
- The **first root-cause signal**:
  - build: first compilation/configuration error (not the cascade)
  - tests: first failing test name + assertion diff / stacktrace
  - lint: rule id + file/line + message
  - crash: full stacktrace + trigger path (screen/action) + device/os if relevant

If evidence is missing, stop and request it explicitly.

### 2) Classify the failure

Classify into one of:
- **build**: Gradle config, dependency resolution, compilation errors
- **tests**: assertion failures, environment issues, flaky tests
- **lint**: style/static analysis violations
- **crash**: runtime exception, NPE, ANR symptoms

Choose the verification command set accordingly (see step 0).

### 3) Guided plan (propose before applying)

Always output (briefly):
- **Most likely root cause** (1-3 bullets, tied to evidence)
- **Minimal fix plan** (smallest change that addresses the root cause)
- **Verification command(s)** you will run
- **Risk note** if the plan includes high-impact changes

Only then apply the changes.

### 4) Apply minimal, rollback-friendly changes

Rules:
- Prefer the smallest code/config change that fixes the issue.
- One iteration targets **one** root cause.
- Avoid destructive/high-risk operations unless necessary.

High-risk changes (require extra justification + rollback path):
- Upgrading Gradle / AGP / Kotlin versions
- Large-scale refactors or sweeping replacements
- Deleting resources/config broadly

### 5) Verify and loop

Run verification commands.
- If **green**: summarize what changed and why it fixed the root cause.
- If **still failing**: return to step 1 with the new evidence. Do not stack unrelated fixes.

## Branch Playbooks

### Build
- Start from the earliest root-cause error block.
- Common buckets: missing import/symbol, dependency mismatch, Gradle plugin/config errors.
- Prefer pinning to the minimal change: fix imports, correct API usage, align versions only when required by evidence.

### Tests
- Confirm the failing test and assertion diff.
- If flaky suspected: rerun the single test, isolate, and document evidence.
- Prefer making behavior deterministic before changing assertions.

### Lint
- Identify the exact rule and offending location(s).
- Apply mechanical fixes where possible; avoid suppressions unless justified.
- Re-run lint to confirm closure.

### Crash
- Use the stacktrace to identify the first app-frame cause.
- Prefer a small fix + add/adjust a test or reproduction steps.
- Verify: build + relevant tests. If reproduction is feasible, rerun the repro.

## Fallback (when blocked)

If you cannot reproduce or evidence is insufficient, stop “auto-fix” and output:
- What evidence is missing (copy/paste checklist)
- The exact command(s) to run to gather it
- A minimal reproduction recipe (if possible)

## Output Format (recommended)

When responding, use:
- **Diagnosis**: root cause + evidence pointer
- **Fix plan**: minimal change(s)
- **Verify**: commands
- **Result**: pass/fail + next step

## Web Lookup Policy

Default: local evidence first.
Use web lookup only when:
- Toolchain/Gradle error semantics are unclear from output, or
- Known dependency incompatibility requires authoritative reference.

## Offline / Restricted Network Environments

If network access is restricted (e.g., `Failed to connect to github.com port 443`):
- Do not block progress on cloning or web lookup.
- Prefer repo-local evidence, cached dependencies, and local docs.
- Offer alternatives:
  - Use an existing local copy of the dependency/repo (if available)
  - Use a provided zip/tarball of the repo instead of `git clone`
  - If mirrors are available in the environment (company Git, Gitee, etc.), use the mirror URL
  - If the fix requires external code and no mirror is available, stop and request the artifact be provided locally

When using web lookup:
- Cite the reason (what local evidence was insufficient)
- Prefer official docs / release notes / issue trackers
