# INVESTMENT 📈

基于 Yahoo Finance 日线数据，计算 **200 日均线（SMA200）** 与 **乖离率（Bias）**，并按金字塔规则输出 **1x～5x** 定投建议的 Java 命令行工具（JDK 21 / Maven）。

## ✨ Highlights

- 🧠 **规则驱动**：按乖离区间输出 1x～5x 倍数，避免情绪化操作。
- 📊 **自动计算**：拉取日线、计算 SMA200、输出最新乖离率与区间。
- 🧱 **可扩展架构**：`MarketDataProvider` 接口可替换行情源。
- 🧪 **单元测试覆盖**：核心策略边界与均线计算已测试。

## 🚀 Quick Start

### 1) 环境要求

- Java 21
- Maven 3.6+
- 可访问外网（Yahoo Finance）

### 2) 构建与测试

```bash
mvn clean test
```

### 3) 运行示例

```bash
mvn -q exec:java "-Dexec.args=--symbol QQQ --range 2y --base-amount 100"
```

## ⚙️ CLI 参数

| 参数 | 说明 | 默认值 |
|------|------|--------|
| `--symbol` | 标的代码（如 `QQQ`、`^IXIC`） | `QQQ` |
| `--range` | Yahoo Chart API 区间，需覆盖至少 200 个交易日 | `2y` |
| `--base-amount` | 可选；基础定投金额，用于换算今日建议投入 | 无 |

## 🧮 策略规则（乖离率金字塔）

乖离率计算：

`(当日收盘 / SMA200 - 1) * 100%`

| 乖离率区间 | 建议倍数 | 区间说明 |
|-----------|----------|----------|
| `>= 0%` | `1x` | 水上 / 基础定投 |
| `[-5%, 0)` | `2x` | 浅度回调 |
| `[-10%, -5%)` | `3x` | 常规回调 |
| `[-20%, -10%)` | `4x` | 深度回调（熊市加仓档） |
| `< -20%` | `5x` | 极度超卖 |

## 🧭 Investment Discipline / 投资纪律

### EN

- Follow rule-based execution: decide by deviation zone, not emotion.
- Keep a stable base DCA amount; only scale by model multiplier (1x-5x).
- Ensure enough cash reserve before entering high-multiplier phases.
- Stay consistent long term; avoid ad-hoc overrides in drawdowns.
- This project is for discipline support only, not financial advice.

### 中文

- 严格按规则执行：根据乖离区间决策，不靠情绪加减仓。
- 保持基础定投金额稳定，仅按模型倍数（1x-5x）进行放大。
- 在高倍加仓阶段前，确保有充足的场外现金储备。
- 坚持长期一致性，不在回撤期做随意的人为改动。
- 本项目仅用于纪律辅助，不构成投资建议。

## 📚 文档导航

更多开发与维护信息见 `.doc` 目录：

| 文档 | 说明 |
|------|------|
| [development.md](.doc/development.md) | 环境要求、构建运行、模块结构、命令行参数与业务规则 |
| [maintenance.md](.doc/maintenance.md) | 依赖与行情源维护、测试、故障排查与迭代记录建议 |
