## ADDED Requirements

### Requirement: Accept bug list input
系统 SHALL 支持用户提供“Bug 清单”作为输入；每条 bug 至少包含一个可识别的标题或编号，并允许附加描述、环境与证据。

#### Scenario: User provides a list of bugs
- **WHEN** 用户一次性提供多条 bug（例如列表/编号/issue 摘要）
- **THEN** 系统 MUST 对每条 bug 进入 triage 流程，并输出逐条的复现结论与下一步

### Requirement: Accept single natural-language bug description
系统 SHALL 支持用户用自然语言描述单条问题作为输入，即使不包含日志，也能进入 triage 并输出最小证据清单与复现尝试。

#### Scenario: User describes a bug without logs
- **WHEN** 用户仅描述现象（无日志/无命令输出）
- **THEN** 系统 MUST 输出最小缺失信息与获取方式，并将该条标记为 `needs-info` 或给出可执行的复现步骤

### Requirement: Produce standardized reproduction report
系统 SHALL 为每条 bug 输出标准化的复现报告，包含：环境、步骤、预期、实际、证据、最小验证命令。

#### Scenario: Bug is reproducible
- **WHEN** triage 过程中确认问题可复现
- **THEN** 系统 MUST 输出完整复现报告，并将状态标记为 `reproducible`

### Requirement: Provide existence conclusion status for each bug
系统 SHALL 为每条 bug 给出一个明确的“存在性结论状态”，至少包含：`reproducible`、`not-reproducible`、`needs-info`、`flaky-suspected`、`environment-blocked`。

#### Scenario: Environment blocks reproduction
- **WHEN** 复现被环境阻塞（例如缺少 JDK/SDK、网络受限导致依赖无法下载）
- **THEN** 系统 MUST 标记状态为 `environment-blocked` 并给出最小修复环境的步骤与验证命令

### Requirement: Gate fixing on reproducibility (or explicit override)
系统 SHOULD 将“进入修复阶段”的默认前置条件设为 `reproducible`；若用户明确要求在不可复现时也修复，系统 MUST 说明风险并记录假设。

#### Scenario: User requests a fix without reproduction
- **WHEN** bug 处于 `needs-info`/`not-reproducible` 状态但用户要求继续修复
- **THEN** 系统 MUST 输出风险说明、关键假设、以及用于未来验收的验证策略

### Requirement: Batch processing and prioritization
当输入为 bug 清单时，系统 SHALL 先输出一个摘要（按类型分组与优先级建议），再逐条输出 triage 结果，避免信息淹没。

#### Scenario: Large bug list provided
- **WHEN** 用户提供较长的 bug 清单
- **THEN** 系统 MUST 先输出分组摘要与处理顺序建议，再开始逐条 triage

