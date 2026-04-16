package org.example.investment;

import java.io.IOException;
import java.util.List;

public interface MarketDataProvider {
    List<DailyBar> fetchDailyBars(String symbol, String range) throws IOException, InterruptedException;
}
