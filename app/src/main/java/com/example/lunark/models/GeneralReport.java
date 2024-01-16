package com.example.lunark.models;

import java.util.Collection;

public class GeneralReport {
    private Collection<DailyReport> dailyReports;
    private Double totalProfit;
    private Long totalReservationCount;

    public Collection<DailyReport> getDailyReports() {
        return dailyReports;
    }

    public void setDailyReports(Collection<DailyReport> dailyReports) {
        this.dailyReports = dailyReports;
    }

    public Double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(Double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Long getTotalReservationCount() {
        return totalReservationCount;
    }

    public void setTotalReservationCount(Long totalReservationCount) {
        this.totalReservationCount = totalReservationCount;
    }
}
