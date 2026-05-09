package org.example.investment;

import java.util.Locale;

public final class RecommendationPrinter {

    private RecommendationPrinter() {}

    public static String format(
            String symbol,
            String range,
            BiasResult bias,
            int multiplier,
            String zoneLabel,
            Double baseAmount
    ) {
        String priceLabel = bias.adjustedClose() ? "复权收盘价" : "收盘价";
        StringBuilder sb = new StringBuilder();
        sb.append("=== 基于乖离率的金字塔定投建议 ===\n");
        sb.append("标的: ").append(symbol).append("  |  数据区间: ").append(range).append("\n");
        sb.append("数据截至: ").append(bias.asOf()).append("（数据源：Yahoo Finance）\n");
        sb.append(String.format(Locale.CHINA, "%s: %.2f%n", priceLabel, bias.latestClose()));
        sb.append(String.format(Locale.CHINA, "200 日均线 (SMA200): %.2f%n", bias.sma200()));
        sb.append(String.format(Locale.CHINA, "乖离率: %.2f%%%n", bias.biasPercent()));
        sb.append("当前区间: ").append(zoneLabel).append("\n");
        sb.append("建议定投倍数: ").append(multiplier).append("x（相对基础定投）\n");
        if (baseAmount != null && baseAmount > 0) {
            double suggested = baseAmount * multiplier;
            sb.append(String.format(Locale.CHINA, "若基础定投金额为 %.2f，则今日建议投入: %.2f%n", baseAmount, suggested));
        }
        sb.append("\n");
        sb.append("提示: 本输出仅供纪律参考，不构成投资建议；连续 4x/5x 会快速消耗现金储备，请量力而行。\n");
        return sb.toString();
    }

    public static void print(
            String symbol,
            String range,
            BiasResult bias,
            int multiplier,
            String zoneLabel,
            Double baseAmount
    ) {
        System.out.print(format(symbol, range, bias, multiplier, zoneLabel, baseAmount));
    }
}
