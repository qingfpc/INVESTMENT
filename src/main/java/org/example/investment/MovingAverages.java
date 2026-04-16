package org.example.investment;

import java.util.List;

public final class MovingAverages {

    private static final int SMA_PERIOD = 200;

    private MovingAverages() {}

    /**
     * Latest bar close vs SMA200 of the last 200 closes (including the latest bar).
     */
    public static BiasResult computeBiasFromBars(List<DailyBar> bars) {
        if (bars.size() < SMA_PERIOD) {
            throw new IllegalArgumentException(
                    "需要至少 " + SMA_PERIOD + " 个交易日，当前仅有 " + bars.size());
        }
        int from = bars.size() - SMA_PERIOD;
        double sum = 0;
        for (int i = from; i < bars.size(); i++) {
            sum += bars.get(i).close();
        }
        double sma200 = sum / SMA_PERIOD;
        DailyBar latest = bars.get(bars.size() - 1);
        double biasPct = (latest.close() / sma200 - 1.0) * 100.0;
        return new BiasResult(latest.date(), latest.close(), sma200, biasPct, latest.adjusted());
    }

    /** Exposed for tests: simple mean of last 200 values in {@code closes}. */
    static double sma200Last(List<Double> closes) {
        if (closes.size() < SMA_PERIOD) {
            throw new IllegalArgumentException("closes size < " + SMA_PERIOD);
        }
        double sum = 0;
        int from = closes.size() - SMA_PERIOD;
        for (int i = from; i < closes.size(); i++) {
            sum += closes.get(i);
        }
        return sum / SMA_PERIOD;
    }
}
