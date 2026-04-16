package org.example;

import org.example.investment.BiasResult;
import org.example.investment.MarketDataProvider;
import org.example.investment.MovingAverages;
import org.example.investment.PyramidStrategy;
import org.example.investment.RecommendationPrinter;
import org.example.investment.YahooChartProvider;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        Map<String, String> opts = parseArgs(args);
        String symbol = opts.getOrDefault("symbol", "QQQ");
        String range = opts.getOrDefault("range", "2y");
        Double baseAmount = null;
        if (opts.containsKey("base-amount")) {
            try {
                baseAmount = Double.parseDouble(opts.get("base-amount"));
            } catch (NumberFormatException e) {
                System.err.println("无效的基础金额: " + opts.get("base-amount"));
                System.exit(2);
                return;
            }
        }

        MarketDataProvider provider = new YahooChartProvider();
        try {
            var bars = provider.fetchDailyBars(symbol, range);
            BiasResult bias = MovingAverages.computeBiasFromBars(bars);
            int mult = PyramidStrategy.multiplier(bias.biasPercent());
            String zone = PyramidStrategy.zoneLabel(bias.biasPercent());
            RecommendationPrinter.print(symbol, range, bias, mult, zone, baseAmount);
        } catch (Exception e) {
            System.err.println("获取行情或计算失败: " + e.getMessage());
            System.exit(1);
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (a.startsWith("--")) {
                String key = a.substring(2);
                if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    map.put(key, args[++i]);
                } else {
                    map.put(key, "true");
                }
            }
        }
        return map;
    }
}
