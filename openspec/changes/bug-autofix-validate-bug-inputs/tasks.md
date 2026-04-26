## 1. 输入协议与输出模板

- [ ] 1.1 在 `bug-autofix-loop` 的 `SKILL.md` 中新增“Bug 输入”章节：支持单条自然语言与多条清单两种输入格式（含最小字段建议）
- [ ] 1.2 定义并写入“复现报告模板”：环境/步骤/预期/实际/证据/最小验证命令
- [ ] 1.3 定义并写入“存在性结论状态”集合：reproducible / not-reproducible / needs-info / flaky-suspected / environment-blocked

## 2. Triage & Repro 阶段（修复前置）

- [ ] 2.1 在 `SKILL.md` 中新增 triage 流程：对每条 bug 先做存在性验证，再决定是否进入修复
- [ ] 2.2 增加“不可复现/证据不足”回退分支：输出最小缺失信息与获取命令（并停止盲修复）
- [ ] 2.3 增加“环境阻塞”分支：对缺少 JDK/SDK/网络受限给出环境修复步骤与验证命令
- [ ] 2.4 增加“疑似 flaky”分支：重跑策略、隔离策略与退出条件

## 3. 批量处理（Bug 清单）

- [ ] 3.1 实现清单摘要输出：按 build/tests/lint/crash/unknown 分组，并给出建议处理顺序（优先 environment-blocked，再 repro）
- [ ] 3.2 实现逐条输出：每条 bug 输出状态 + 复现报告（或 needs-info 清单）

## 4. 示例与验证

- [ ] 4.1 更新 `examples.md`：增加“Bug 清单输入”的示例（至少 3 条：可复现/证据不足/环境阻塞）
- [ ] 4.2 更新 `examples.md`：增加“单条自然语言输入”的示例（无日志→needs-info→补齐后可复现）
