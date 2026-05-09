package org.example;

import org.example.investment.BiasResult;
import org.example.investment.MarketDataProvider;
import org.example.investment.MovingAverages;
import org.example.investment.PyramidStrategy;
import org.example.investment.RecommendationPrinter;
import org.example.investment.ReportStore;
import org.example.investment.YahooChartProvider;

import java.io.IOException;
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
        StringBuilder output = new StringBuilder();

        // 主标的分析
        BiasResult primaryBias;
        try {
            primaryBias = runAnalysis(provider, symbol, range, baseAmount, output);
        } catch (Exception e) {
            System.err.println("获取行情或计算失败: " + e.getMessage());
            System.exit(1);
            return;
        }

        // 黄金（GLD）分析
        String goldSymbol = "GLD";
        if (!symbol.equals(goldSymbol)) {
            output.append("\n");
            try {
                runAnalysis(provider, goldSymbol, range, null, output);
            } catch (Exception e) {
                System.err.println("获取黄金行情或计算失败: " + e.getMessage());
            }
        }

        System.out.print(output.toString());

        try {
            ReportStore.save(primaryBias.asOf(), output.toString());
        } catch (IOException e) {
            System.err.println("保存记录文件失败: " + e.getMessage());
        }
    }

    private static BiasResult runAnalysis(MarketDataProvider provider, String symbol, String range, Double baseAmount, StringBuilder out)
            throws IOException, InterruptedException {
        var bars = provider.fetchDailyBars(symbol, range);
        BiasResult bias = MovingAverages.computeBiasFromBars(bars);
        int mult = PyramidStrategy.multiplier(bias.biasPercent());
        String zone = PyramidStrategy.zoneLabel(bias.biasPercent());
        out.append(RecommendationPrinter.format(symbol, range, bias, mult, zone, baseAmount));
        return bias;
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
