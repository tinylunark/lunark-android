package com.example.lunark.models;

public class HostReportEligibility {
    private Long hostId;
    private Boolean eligible;

    public HostReportEligibility() {
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public Boolean isEligible() {
        return eligible;
    }

    public void setEligible(Boolean eligible) {
        this.eligible = eligible;
    }
}
