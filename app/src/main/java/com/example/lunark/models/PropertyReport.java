package com.example.lunark.models;

import java.util.Collection;

public class PropertyReport {
    private Collection<MonthlyReport> monthlyReports;

    public Collection<MonthlyReport> getMonthlyReports() {
        return monthlyReports;
    }

    public void setMonthlyReports(Collection<MonthlyReport> monthlyReports) {
        this.monthlyReports = monthlyReports;
    }
}
