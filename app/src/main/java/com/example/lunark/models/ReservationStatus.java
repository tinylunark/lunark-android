package com.example.lunark.models;

public enum ReservationStatus {
    ALL("ALL"),
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED");

    private String name;

    ReservationStatus(String name) {
        this.name = name;
    }

    public static ReservationStatus fromString(String value) {
        for (ReservationStatus status : ReservationStatus.values()) {
            if (status.name.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid string in reservation status");
    }

    @Override
    public String toString() {
        return this.name;
    }
}
