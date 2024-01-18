package com.example.lunark.dtos;

import java.time.LocalDate;

public class CreateReservationDto {
    private Long propertyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfGuests;

    public CreateReservationDto(Long propertyId, LocalDate startDate, LocalDate endDate, Integer numberOfGuests) {
        this.propertyId = propertyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
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

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
