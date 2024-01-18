package com.example.lunark.models;

import java.util.Date;

public class Reservation {
    private long id;
    private Date startDate;
    private Date endDate;
    private int numberOfGuests;
    private ReservationStatus status;
    private double price;
    private Property property;
    private Profile guest;
    private long propertyId;
    private String propertyName;
    private long guestId;


    public Reservation() {
    }

    public Reservation(long id, Date startDate, Date endDate, int numberOfGuests, ReservationStatus status, double price, Property property, Profile guest, long propertyId, String propertyName, long guestId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.price = price;
        this.property = property;
        this.guest = guest;
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.guestId = guestId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Profile getGuest() {
        return guest;
    }

    public void setGuest(Profile guest) {
        this.guest = guest;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public long getGuestId() {
        return guestId;
    }

    public void setGuestId(long guestId) {
        this.guestId = guestId;
    }
}
