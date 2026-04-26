# Examples

## Example 1: Build failure (Gradle / Kotlin compile)

**User input:**
> 自动修复 bug：构建失败。  
> 我跑 `./gradlew assembleDebug` 报错：`Unresolved reference: fooBar` in `MainActivity.kt:42`

**Expected workflow:**
- Collect evidence: exact command + first root-cause error block.
- Classify: build.
- Propose:
  - Root cause: symbol removed/renamed or missing import/dependency.
  - Minimal fix: update API usage or add missing dependency/import.
  - Verify: `./gradlew assembleDebug`.
- Apply and re-run verify.

**Recommended response format:**
- **Diagnosis**: `fooBar` 未定义（编译期根因），位于 `MainActivity.kt:42`
- **Fix plan**: 查找 `fooBar` 的定义/替代 API，并最小化修改调用点
- **Verify**: `./gradlew assembleDebug`
- **Result**: 通过/失败（失败则贴首个根因并进入下一轮）

## Example 2: Unit test failure

**User input:**
> 修测试失败：`SomeRepositoryTest#load_returnsCachedValue` 断言失败，期望 3 实际 0。

**Expected workflow:**
- Collect evidence: failing test name + assertion diff/stacktrace.
- Classify: tests.
- Propose:
  - Root cause hypothesis tied to evidence (e.g., cache not initialized).
  - Minimal fix vs adjust expectation (prefer determinism).
  - Verify: `./gradlew testDebugUnitTest --tests "*SomeRepositoryTest*load_returnsCachedValue"`.
- Apply and re-run verify.

## Example 3: Lint failure

**User input:**
> 修 lint：`UnusedResources` 失败，提示 `res/drawable/old_icon.xml` 未使用。

**Expected workflow:**
- Collect evidence: rule id + file path + message.
- Classify: lint.
- Propose:
  - Minimal fix: remove unused resource or reference it if intended.
  - Verify: `./gradlew lintDebug`.
- Apply and re-run verify.

## Example 4: Crash (stacktrace)

**User input:**
> 修崩溃：点击“搜索”闪退。堆栈第一行：`java.lang.NullPointerException at SearchViewModel.onQuery(...)`

**Expected workflow:**
- Collect evidence: full stacktrace + reproduction steps + environment.
- Classify: crash.
- Propose:
  - Root cause: null value not handled in `onQuery`.
  - Minimal fix: null guard / default value / adjust state init.
  - Verify: reproduce + `./gradlew testDebugUnitTest` (and build).
- Apply and re-run verify.

## Example 5: Build toolchain blocked (JAVA_HOME missing)

**User input / evidence:**
> 我下载了 `LiveUpdateNotification`，运行 `.\gradlew.bat -q tasks` 失败：  
> `ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.`

**Expected workflow:**
- Collect evidence: the exact command + the error block.
- Classify: build (toolchain/environment).
- Propose (guided):
  - **Root cause**: 本机未配置可用 JDK（Android Studio/Gradle 无法找到 `java.exe`）。
  - **Minimal fix plan**:
    1) 在 Android Studio 中选择可用 JDK（优先使用 IDE 自带 JBR/JDK），或安装 JDK 17 并配置 `JAVA_HOME`  
    2) 重开终端/IDE 让环境变量生效  
  - **Verify**: 重新运行 `.\gradlew.bat -q tasks`（以及后续 `assembleDebug`/`test`/`lint`）
- Apply: 由于这是“用户机器环境配置”，agent 不能替用户直接改系统环境变量；应给出具体可操作步骤并等待用户执行，再继续下一轮验证。

## Example 6: Bug 清单输入（批量 triage：可复现 / 证据不足 / 环境阻塞）

**User input:**
> 我这有个 bug 清单，帮我处理一下：  
> 1) #101 搜索页点击“提交”闪退（Android 14，小米 13）  
> 2) #102 首页偶尔列表为空（无日志）  
> 3) #103 本地跑不起来：`./gradlew testDebugUnitTest` 报 `JAVA_HOME is not set`

**Expected workflow:**
- 先摘要分组：
  - crash: #101
  - unknown/needs-info: #102
  - build(environment): #103
- 建议顺序：先处理 #103（environment-blocked），再处理 #101（repro），最后 #102（needs-info）
- 逐条输出状态与下一步：
  - #103：`environment-blocked`，给出安装/选择 JDK + 验证命令 `./gradlew -q tasks` / `./gradlew testDebugUnitTest`
  - #101：尝试复现并输出复现报告（环境/步骤/预期/实际/证据/最小验证命令），状态 `reproducible` 后再进入修复闭环
  - #102：标记 `needs-info`，输出最小缺失信息与获取方式（例如：出现频率、是否需要登录、接口/本地缓存、相关日志抓取方式）

## Example 7: 单条自然语言输入（无日志 → needs-info → 补齐后可复现）

**User input (round 1):**
> App 有时候卡住不动了，帮我修一下。

**Expected workflow (round 1):**
- 由于无证据，先进入 triage：
  - 输出状态：`needs-info`
  - 输出最小缺失信息清单（示例）：
    - 发生在哪个页面/操作路径（可编号步骤）
    - 设备/系统版本/应用版本
    - 是否必现、频率、是否与网络/权限相关
    - 如果是“卡住”：是否有 ANR 对话框、logcat 关键日志（如可提供）
  - 给出获取方式/命令（按项目类型选择，例如 Android：如何抓 logcat、如何导出崩溃/ANR）

**User input (round 2, user provides evidence):**
> 必现：打开“设置”页，点“同步”后卡住，约 5 秒后弹 ANR。logcat 有：`Input dispatching timed out ...`，堆栈指向 `SyncManager.syncBlocking(...)`

**Expected workflow (round 2):**
- 输出状态：`reproducible`
- 输出复现报告模板（环境/步骤/预期/实际/证据/最小验证命令）
- 然后进入修复闭环：
  - Diagnosis：主线程阻塞导致 ANR（证据：ANR/logcat/调用点）
  - Fix plan：将阻塞 IO/网络移出主线程、添加超时/取消
  - Verify：复现步骤 + 相关测试/构建命令

