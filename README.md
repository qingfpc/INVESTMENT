# INVESTMENT

基于 Yahoo Finance 日线数据计算 **200 日均线与乖离率**，按金字塔规则输出 **1x～5x** 定投倍数建议的 Java 命令行工具（JDK 21 / Maven）。

## Investment Discipline / 投资纪律

### EN

- Follow rule-based execution: decide by deviation zone, not by emotion.
- Keep a stable base DCA amount and only scale by the model multiplier (1x-5x).
- Ensure sufficient off-market cash reserve before running high-multiplier phases.
- Keep long-term consistency; avoid ad-hoc override during drawdowns.
- This tool is for discipline support only, not financial advice.

### 中文

- 严格按规则执行：根据乖离区间决策，不靠情绪加减仓。
- 保持基础定投金额稳定，仅按模型倍数（1x-5x）进行放大。
- 在高倍加仓阶段前，确保有充足的场外现金储备。
- 坚持长期一致性，不在回撤期做随意的人为改动。
- 本工具仅用于纪律辅助，不构成投资建议。

详细设计与维护说明见 `.doc` 目录：

| 文档 | 说明 |
|------|------|
| [development.md](.doc/development.md) | 环境要求、构建运行、模块结构、命令行参数与业务规则 |
| [maintenance.md](.doc/maintenance.md) | 依赖与行情源维护、测试、故障排查与迭代记录建议 |
