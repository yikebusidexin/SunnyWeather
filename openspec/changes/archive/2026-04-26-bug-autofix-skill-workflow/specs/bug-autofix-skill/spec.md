## ADDED Requirements

### Requirement: Skill can be discovered and triggered
系统 SHALL 提供一个项目级 Cursor Skill，其 `name`、`description` 与正文内容明确包含用于发现与触发的关键词（例如：自动修复 bug、修 CI、修构建失败、修测试失败、修 lint、修崩溃）。

#### Scenario: User requests bug auto-fix
- **WHEN** 用户请求“自动修复 bug/缺陷”或表达等价意图（修 CI / 修 build / 修 tests / 修 lint / 修 crash）
- **THEN** Skill SHALL 指导 agent 进入标准化的“诊断-修复-验证”闭环

### Requirement: Evidence-first diagnosis
Skill SHALL 要求在提出修复方案前先收集最小可用证据集：复现命令、关键错误输出（包含首个根因/首个失败用例/首个异常栈）以及相关环境信息（如适用）。

#### Scenario: Build fails with Gradle output
- **WHEN** 用户提供 Gradle 构建失败输出或要求修复构建失败
- **THEN** Skill MUST 指导 agent 先确定可复现的 Gradle 任务与失败根因片段，再提出修复方案

### Requirement: Failure classification and branching
Skill SHALL 将缺陷归类为至少四种类型，并对每类进入不同的验证与修复分支：`build`、`tests`、`lint`、`crash`。

#### Scenario: Lint violations detected
- **WHEN** 错误属于 lint/静态检查违规
- **THEN** Skill MUST 指导 agent 采用“规则 → 具体违规点 → 最小修复 → 重新运行 lint”的路径

### Requirement: Guided execution mode
Skill SHALL 采用 guided 模式：在进行代码改动前先输出“最可能根因 + 最小修复方案 + 验证命令”，随后再执行改动并回报验证结果。

#### Scenario: Proposed fix requires code changes
- **WHEN** 诊断结果表明需要修改代码或配置
- **THEN** Skill MUST 先给出修复计划与验证命令，再进行改动与验证

### Requirement: Verification is the definition of done
Skill SHALL 将“验证命令通过”定义为完成标准，并根据缺陷类型选择默认验证命令集合；同时允许项目按需配置完成条件。

#### Scenario: Fixing unit test failure
- **WHEN** 缺陷被归类为 tests 失败
- **THEN** Skill MUST 指导 agent 运行（或建议运行）对应测试命令，并以通过/失败作为闭环判定

### Requirement: Safety guardrails and rollback-friendly changes
Skill SHALL 规定安全护栏：最小改动优先、一次迭代只解决一个根因、避免破坏性操作；对高风险改动必须给出风险与回退路径。

#### Scenario: Proposed fix involves toolchain upgrade
- **WHEN** 修复方案涉及 Kotlin/AGP/Gradle 版本升级或大范围替换
- **THEN** Skill MUST 先说明风险、备选方案与回退策略，再决定是否执行

### Requirement: Failure fallback behavior
当证据不足或连续迭代仍无法修复时，Skill SHALL 收敛输出为可执行的下一步：需要补充的证据、最小复现步骤、或建议新增/调整的测试用例。

#### Scenario: Cannot reproduce with given info
- **WHEN** 用户未提供足够信息导致无法复现或定位根因
- **THEN** Skill MUST 列出需要补充的信息与最小复现命令，并停止“盲修复”
