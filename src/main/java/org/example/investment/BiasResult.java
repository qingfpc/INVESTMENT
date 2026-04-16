package org.example.investment;

import java.time.LocalDate;

public record BiasResult(
        LocalDate asOf,
        double latestClose,
        double sma200,
        double biasPercent,
        boolean adjustedClose) {
}
