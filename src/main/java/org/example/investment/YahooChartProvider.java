package org.example.investment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Fetches daily bars from Yahoo Finance chart API (v8). No API key; for personal use only.
 */
public final class YahooChartProvider implements MarketDataProvider {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36";
    private static final ZoneId NY = ZoneId.of("America/New_York");

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public YahooChartProvider() {
        this(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build(), new ObjectMapper());
    }

    YahooChartProvider(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<DailyBar> fetchDailyBars(String symbol, String range) throws IOException, InterruptedException {
        String enc = URLEncoder.encode(symbol, StandardCharsets.UTF_8);
        URI uri = URI.create("https://query1.finance.yahoo.com/v8/finance/chart/" + enc + "?interval=1d&range=" + range);

        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(45))
                .header("User-Agent", USER_AGENT)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Yahoo chart HTTP " + response.statusCode() + " for " + symbol);
        }

        JsonNode root = objectMapper.readTree(response.body());
        JsonNode results = root.path("chart").path("result");
        if (!results.isArray() || results.isEmpty()) {
            JsonNode error = root.path("chart").path("error");
            String msg = error.isMissingNode() ? "empty result" : error.toString();
            throw new IOException("Yahoo chart: " + msg);
        }

        JsonNode result = results.get(0);
        JsonNode timestamps = result.path("timestamp");
        if (!timestamps.isArray() || timestamps.isEmpty()) {
            throw new IOException("Yahoo chart: no timestamps");
        }

        JsonNode indicators = result.path("indicators");
        CloseSeries closeSeries = extractCloseSeries(indicators);

        List<DailyBar> bars = new ArrayList<>();
        int n = timestamps.size();
        int m = closeSeries.values().size();
        int len = Math.min(n, m);
        for (int i = 0; i < len; i++) {
            JsonNode tsNode = timestamps.get(i);
            if (tsNode == null || !tsNode.canConvertToLong()) {
                continue;
            }
            Double c = closeSeries.values().get(i);
            if (c == null || c.isNaN() || c <= 0) {
                continue;
            }
            long epochSec = tsNode.asLong();
            LocalDate date = Instant.ofEpochSecond(epochSec).atZone(NY).toLocalDate();
            bars.add(new DailyBar(date, c, closeSeries.adjusted()));
        }

        bars.sort((a, b) -> a.date().compareTo(b.date()));
        return bars;
    }

    private record CloseSeries(List<Double> values, boolean adjusted) {}

    private static CloseSeries extractCloseSeries(JsonNode indicators) {
        JsonNode adjBlock = indicators.path("adjclose");
        if (adjBlock.isArray() && !adjBlock.isEmpty()) {
            JsonNode adjclose = adjBlock.get(0).path("adjclose");
            if (adjclose.isArray()) {
                return new CloseSeries(jsonArrayToDoubles(adjclose), true);
            }
        }
        JsonNode quoteBlock = indicators.path("quote");
        if (quoteBlock.isArray() && !quoteBlock.isEmpty()) {
            JsonNode close = quoteBlock.get(0).path("close");
            if (close.isArray()) {
                return new CloseSeries(jsonArrayToDoubles(close), false);
            }
        }
        return new CloseSeries(List.of(), false);
    }

    private static List<Double> jsonArrayToDoubles(JsonNode array) {
        List<Double> out = new ArrayList<>(array.size());
        for (JsonNode n : array) {
            if (n == null || n.isNull()) {
                out.add(null);
            } else {
                out.add(n.asDouble());
            }
        }
        return out;
    }
}
