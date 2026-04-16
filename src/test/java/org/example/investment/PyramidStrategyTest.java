package org.example.investment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PyramidStrategyTest {

    @ParameterizedTest
    @CsvSource({
            "1, 0",
            "1, 5.5",
            "2, -0.01",
            "2, -2.5",
            "2, -5",
            "3, -5.01",
            "3, -7",
            "3, -10",
            "4, -10.01",
            "4, -15",
            "4, -20",
            "5, -20.01",
            "5, -50"
    })
    void multiplierBoundaries(int expected, double bias) {
        assertEquals(expected, PyramidStrategy.multiplier(bias));
    }

    @Test
    void zoneLabelsMatchTiers() {
        assertEquals("水上/基础定投", PyramidStrategy.zoneLabel(0));
        assertEquals("浅度回调", PyramidStrategy.zoneLabel(-1));
        assertEquals("常规回调", PyramidStrategy.zoneLabel(-6));
        assertEquals("深度回调（熊市加仓档）", PyramidStrategy.zoneLabel(-11));
        assertEquals("极度超卖", PyramidStrategy.zoneLabel(-21));
    }
}
