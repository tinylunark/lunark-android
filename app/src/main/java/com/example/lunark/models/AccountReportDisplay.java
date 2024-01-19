package com.example.lunark.models;

public class AccountReportDisplay {
    Long id;
    String date;
    Long reporterId;
    Long reportedId;
    String reason;

    public AccountReportDisplay() {

    }

    public AccountReportDisplay(Long id, String date, Long reporterId, Long reportedId, String reason) {
        this.id = id;
        this.date = date;
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public Long getReportedId() {
        return reportedId;
    }

    public void setReportedId(Long reportedId) {
        this.reportedId = reportedId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
