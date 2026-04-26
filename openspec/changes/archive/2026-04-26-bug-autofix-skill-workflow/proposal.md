## Why

当前缺少一套可复用、可验证的“自动修复 bug/缺陷”的工程化流程，导致构建失败、测试失败、lint 违规或崩溃类问题修复依赖人工经验且效率不稳定。建立一个标准化的 Cursor Skill 流程，可以把“诊断-修复-验证”闭环固化下来，提升修复速度与一致性。

## What Changes

- 定义一个项目级 Cursor Skill：覆盖构建失败、测试失败、lint 失败与运行时崩溃四类问题的自动修复工作流（guided：先诊断给方案，再执行改动）
- 规定输入输出与触发词（例如“修 CI”“修构建失败”“自动修复 bug”“修 lint”“修崩溃”）
- 建立安全护栏与回退策略：最小改动优先、可回滚、必要时才查外部资料
- 给出在 Android/Gradle 项目中的默认验证命令与可配置点（例如 build/test/lint 的完成条件）

## Capabilities

### New Capabilities

- `bug-autofix-skill`: 定义“自动修复 bug 的 skill 的流程规范”（触发、证据收集、分类、修复策略、验证闭环、护栏与失败回退），并规定生成的项目技能文件结构与内容要求

### Modified Capabilities

<!-- 无既有 specs，可留空 -->

## Impact

- `.cursor/skills/`：会新增一个可共享的 skill 目录与文档（可能包含可选的 examples/reference 文件）
- 终端/命令：需要可运行的验证命令（如 Gradle build/test/lint），并在流程中固化为“完成标准”
- 团队协作：统一 bug 修复策略与输出格式，降低不同人修复同类问题的差异
