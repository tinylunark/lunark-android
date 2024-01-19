package com.example.lunark.models;

public class ReviewReport {
    private Long id;
    private String date;
    private Long reporterId;
    private Long reviewId;

    public ReviewReport() {

    }

    public ReviewReport(Long id, String date, Long reporterId, Long reviewId) {
        this.id = id;
        this.date = date;
        this.reporterId = reporterId;
        this.reviewId = reviewId;
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

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }
}
