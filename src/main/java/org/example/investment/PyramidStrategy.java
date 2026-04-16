package org.example.investment;

/**
 * 基于乖离率的金字塔定投倍数（1x～5x）与区间中文名。
 */
public final class PyramidStrategy {

    private PyramidStrategy() {}

    public static int multiplier(double biasPercent) {
        if (biasPercent >= 0) {
            return 1;
        }
        if (biasPercent >= -5) {
            return 2;
        }
        if (biasPercent >= -10) {
            return 3;
        }
        if (biasPercent >= -20) {
            return 4;
        }
        return 5;
    }

    public static String zoneLabel(double biasPercent) {
        if (biasPercent >= 0) {
            return "水上/基础定投";
        }
        if (biasPercent >= -5) {
            return "浅度回调";
        }
        if (biasPercent >= -10) {
            return "常规回调";
        }
        if (biasPercent >= -20) {
            return "深度回调（熊市加仓档）";
        }
        return "极度超卖";
    }
}
