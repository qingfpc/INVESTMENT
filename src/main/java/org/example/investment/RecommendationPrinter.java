package org.example.investment;

import java.util.Locale;

public final class RecommendationPrinter {

    private RecommendationPrinter() {}

    public static void print(
            String symbol,
            String range,
            BiasResult bias,
            int multiplier,
            String zoneLabel,
            Double baseAmount
    ) {
        String priceLabel = bias.adjustedClose() ? "复权收盘价" : "收盘价";
        System.out.println("=== 基于乖离率的金字塔定投建议 ===");
        System.out.println("标的: " + symbol + "  |  数据区间: " + range);
        System.out.println("数据截至: " + bias.asOf() + "（数据源：Yahoo Finance）");
        System.out.printf(Locale.CHINA, "%s: %.2f%n", priceLabel, bias.latestClose());
        System.out.printf(Locale.CHINA, "200 日均线 (SMA200): %.2f%n", bias.sma200());
        System.out.printf(Locale.CHINA, "乖离率: %.2f%%%n", bias.biasPercent());
        System.out.println("当前区间: " + zoneLabel);
        System.out.println("建议定投倍数: " + multiplier + "x（相对基础定投）");
        if (baseAmount != null && baseAmount > 0) {
            double suggested = baseAmount * multiplier;
            System.out.printf(Locale.CHINA, "若基础定投金额为 %.2f，则今日建议投入: %.2f%n", baseAmount, suggested);
        }
        System.out.println();
        System.out.println("提示: 本输出仅供纪律参考，不构成投资建议；连续 4x/5x 会快速消耗现金储备，请量力而行。");
    }
}
