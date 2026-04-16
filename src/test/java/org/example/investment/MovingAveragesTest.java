package org.example.investment;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovingAveragesTest {

    @Test
    void sma200Last_uniformSeries() {
        List<Double> closes = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            closes.add(100.0);
        }
        assertEquals(100.0, MovingAverages.sma200Last(closes), 1e-9);
    }

    @Test
    void sma200Last_linearTail() {
        List<Double> closes = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            closes.add((double) i);
        }
        double expected = (1 + 200) / 2.0;
        assertEquals(expected, MovingAverages.sma200Last(closes), 1e-9);
    }

    @Test
    void computeBias_matchesExampleScale() {
        List<DailyBar> bars = new ArrayList<>();
        LocalDate d = LocalDate.of(2020, 1, 2);
        for (int i = 0; i < 199; i++) {
            bars.add(new DailyBar(d.plusDays(i), 594.95, true));
        }
        bars.add(new DailyBar(d.plusDays(199), 584.98, true));
        BiasResult r = MovingAverages.computeBiasFromBars(bars);
        assertEquals(584.98, r.latestClose(), 1e-9);
        assertEquals(594.90015, r.sma200(), 0.001);
        assertEquals(-1.67, r.biasPercent(), 0.05);
        assertEquals(2, PyramidStrategy.multiplier(r.biasPercent()));
    }

    @Test
    void computeBias_requires200Bars() {
        List<DailyBar> bars = List.of(new DailyBar(LocalDate.now(), 1, true));
        assertThrows(IllegalArgumentException.class, () -> MovingAverages.computeBiasFromBars(bars));
    }
}
