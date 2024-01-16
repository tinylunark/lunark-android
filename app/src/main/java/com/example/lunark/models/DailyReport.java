package com.example.lunark.models;

import java.time.LocalDate;
import java.util.Collection;

public class DailyReport {
    private LocalDate date;
    private Double profit;
    private Long reservationCount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Long getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(Long reservationCount) {
        this.reservationCount = reservationCount;
    }
}
