package com.example.lunark.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AccountReport {

    private Long reportedId;
    private LocalDateTime date;
    private String reason;

    public AccountReport() {
    }

    public AccountReport(Long reportedId, String reason) {
        this.reportedId = reportedId;
        this.reason = reason;
        this.date = LocalDateTime.now();
    }

    public Long getReportedId() {
        return reportedId;
    }

    public void setReportedId(Long reportedId) {
        this.reportedId = reportedId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
