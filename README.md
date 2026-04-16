# INVESTMENT 📈

**Read this in other languages: [English](README.md), [中文](README_zh.md).**

A Java CLI tool that fetches daily market data from Yahoo Finance, calculates **200-day moving average (SMA200)** and **bias**, and outputs **1x-5x** pyramid DCA suggestions based on deviation zones (JDK 21 / Maven).

## ✨ Highlights

- 🧠 **Rule-based execution**: decide by bias zone, not by emotion.
- 📊 **Automatic calculation**: fetch daily bars, compute SMA200, and print the latest bias and zone.
- 🧱 **Extensible architecture**: swap data source via the `MarketDataProvider` interface.
- 🧪 **Test coverage**: core strategy boundaries and moving-average logic are covered.

## 🚀 Quick Start

### 1) Requirements

- Java 21
- Maven 3.6+
- Internet access (Yahoo Finance)

### 2) Build & test

```bash
mvn clean test
```

### 3) Run example

```bash
mvn -q exec:java "-Dexec.args=--symbol QQQ --range 2y --base-amount 100"
```

## ⚙️ CLI Arguments

| Argument | Description | Default |
|----------|-------------|---------|
| `--symbol` | Ticker symbol (e.g. `QQQ`, `^IXIC`) | `QQQ` |
| `--range` | Yahoo Chart API range; must cover at least 200 trading days | `2y` |
| `--base-amount` | Optional base DCA amount for today's suggested amount | none |

## 🧮 Strategy Rules (Bias-based Pyramid)

Bias formula:

`(close / SMA200 - 1) * 100%`

| Bias Range | Multiplier | Zone |
|------------|------------|------|
| `>= 0%` | `1x` | Above water / baseline DCA |
| `[-5%, 0)` | `2x` | Mild pullback |
| `[-10%, -5%)` | `3x` | Normal pullback |
| `[-20%, -10%)` | `4x` | Deep pullback (bear-market add-on tier) |
| `< -20%` | `5x` | Extreme oversold |

## 🧭 Investment Discipline

- Follow rule-based execution: decide by deviation zone, not emotion.
- Keep a stable base DCA amount; only scale by model multiplier (1x-5x).
- Ensure enough cash reserve before entering high-multiplier phases.
- Stay consistent long term; avoid ad-hoc overrides during drawdowns.
- This project is for discipline support only, not financial advice.

## 📚 Documentation

See `.doc` for development and maintenance docs:

| Doc | Description |
|-----|-------------|
| [development.md](.doc/development.md) | Setup, build/run, module design, CLI args, and strategy rules |
| [maintenance.md](.doc/maintenance.md) | Dependency/API maintenance, troubleshooting, and update notes |
