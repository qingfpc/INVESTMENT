package org.example.investment;

import java.time.LocalDate;

/**
 * One trading day: date and closing price (adjusted close when available).
 */
public record DailyBar(LocalDate date, double close, boolean adjusted) {
}
