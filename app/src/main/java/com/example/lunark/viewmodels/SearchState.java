package com.example.lunark.viewmodels;

import com.example.lunark.models.Property;

import java.time.LocalDate;
import java.util.List;

public class SearchState {
    private String location;
    private Integer guestNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> amenityIds;
    private Property.PropertyType type;
    private Double minPrice;
    private Double maxPrice;

    public SearchState() {}

    public SearchState(String location, Integer guestNumber, LocalDate startDate, LocalDate endDate, List<Long> amenityIds, Property.PropertyType type, Double minPrice, Double maxPrice) {
        this.location = location;
        this.guestNumber = guestNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amenityIds = amenityIds;
        this.type = type;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(Integer guestNumber) {
        this.guestNumber = guestNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Long> getAmenityIds() {
        return amenityIds;
    }

    public void setAmenityIds(List<Long> amenityIds) {
        this.amenityIds = amenityIds;
    }

    public Property.PropertyType getType() {
        return type;
    }

    public void setType(Property.PropertyType type) {
        this.type = type;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
