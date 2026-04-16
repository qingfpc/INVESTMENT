# 维护文档

## 维护清单（建议节奏）

| 项 | 建议 | 说明 |
|----|------|------|
| 依赖安全与版本 | 每季度或遇 CVE 时 | 升级 `jackson-databind`、`junit-jupiter`，跑 `mvn test` |
| Yahoo API 行为 | 拉取失败或解析异常时 | 对照实际 JSON 调整 `YahooChartProvider`字段路径 |
| JDK/Maven | 升级工具链后 | 确认 `pom.xml` 中 `maven.compiler.source/target` 与 CI/本地一致 |

## 行情源（Yahoo Finance）

- **风险**：非官方保障接口，可能出现限流、结构变更、地区访问差异；仅作个人纪律辅助，不构成投资建议。
- **常见现象**：
  - HTTP 非 200：检查网络、代理、User-Agent 是否仍被接受。
  - `empty result` /无 `timestamp`：标的代码错误或该市场无数据。
  - `adjclose` 缺失：程序会回退到 `quote.close`，输出中会标注为「收盘价」而非「复权收盘价」。
- **扩展**：若需更换数据源，可实现 `MarketDataProvider`，在 `Main` 中注入新实现，并保持 `DailyBar` 语义一致（按交易日、收盘价序列）。

## 故障排查

1. **单元测试通过但运行无输出/报错**  
   多为网络或 Yahoo 拒绝请求；确认本机可访问 `query1.finance.yahoo.com`。

2. **中文乱码**  
   已设置 UTF-8 的 `System.out/err`；终端可尝试 `chcp 65001`，或将输出重定向到文件用 UTF-8 编辑器查看。

3. **数据不足 200 根 K 线**  
调大 `--range`（如 `5y`），或确认标的上市时间足够长。

4. **PowerShell 下 Maven 参数被拆断**  
   使用：`mvn -q exec:java "-Dexec.args=--symbol QQQ --range 2y"`。

## 迭代记录（人工维护）

在下方按日期追加简短记录，便于后续接手人员了解变更动机。

```text
（示例）
YYYY-MM-DD：说明变更摘要；关联 issue/PR（如有）。
```
