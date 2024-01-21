package com.example.lunark.models;

import java.time.LocalDate;

public class AvailabilityEntry {
    private LocalDate date;
    private double price;
    private boolean reserved;

    public AvailabilityEntry() {
    }

    public AvailabilityEntry(LocalDate date, double price) {
        this.date = date;
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }
}
