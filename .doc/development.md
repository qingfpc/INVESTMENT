# 开发文档

## 项目简介

基于纳斯达克相关标的（默认 QQQ）的日线行情，计算 **200 日简单均线（SMA200）** 与 **乖离率**，按预设五档规则输出 **1x～5x** 金字塔定投倍数及中文区间说明。行情来源为 Yahoo Finance Chart API（v8），无需 API Key，适合个人本地脚本使用。

## 环境要求

- JDK **21**
- Maven **3.6+**
- 可访问外网（拉取 Yahoo 行情）

## 构建与测试

```text
mvn clean test
mvn clean package
```

## 运行方式

入口类：`org.example.Main`。

使用 Maven 执行（Windows PowerShell 下 `exec.args` 建议整体加引号）：

```text
mvn -q exec:java "-Dexec.args=--symbol QQQ --range 2y --base-amount 100"
```

也可在 IDE 中直接运行 `Main`，程序参数示例：`--symbol ^IXIC --range 2y`。

### 命令行参数

| 参数 | 说明 | 默认 |
|------|------|------|
| `--symbol` | Yahoo 标的代码，如 `QQQ`、`^IXIC` | `QQQ` |
| `--range` | Chart API 的 `range`，需保证足够覆盖 200 个交易日 | `2y` |
| `--base-amount` | 可选；基础定投金额，用于换算「今日建议投入」 | 无 |

程序启动时会将标准输出/错误流设为 **UTF-8**，减轻中文乱码。若 Windows 终端仍异常，可先执行 `chcp 65001`。

## 模块与包结构

- `org.example.Main`：CLI 解析、组装流程、异常退出码。
- `org.example.investment`：
  - `DailyBar`：单日收盘价与是否复权口径。
  - `MarketDataProvider` / `YahooChartProvider`：HTTP 拉取与 JSON 解析（Jackson），优先 `adjclose`，否则 `close`。
  - `MovingAverages`：最近 200 根 K 线 SMA200 与乖离率。
  - `BiasResult`：最新交易日、收盘价、SMA200、乖离率等计算结果。
  - `PyramidStrategy`：乖离率档位 → 倍数与中文区间名。
  - `RecommendationPrinter`：控制台中文报告与风险提示。

## 业务规则（档位）

乖离率 \(=\) \((当日收盘 / \text{SMA200} - 1) \times 100\%\)。

| 乖离率 | 倍数 | 区间名称 |
|--------|------|----------|
| ≥ 0% | 1x | 水上/基础定投 |
| [-5%, 0) | 2x | 浅度回调 |
| [-10%, -5%) | 3x | 常规回调 |
| [-20%, -10%) | 4x | 深度回调（熊市加仓档） |
| < -20% | 5x | 极度超卖 |

## 依赖（Maven）

- `jackson-databind`：解析 Yahoo JSON。
- `junit-jupiter`：单元测试（scope test）。

详见仓库根目录 `pom.xml`。

## 测试说明

- `PyramidStrategyTest`：倍数与区间边界（含 -20、-10、-5、0 邻近值）。
- `MovingAveragesTest`：SMA 小样本与合成 K 线下的乖离率/档位。
