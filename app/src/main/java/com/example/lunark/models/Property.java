package com.example.lunark.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Property {
    private Long id;
    private String name;
    private String description;
    private Address address;
    private int minGuests;
    private int maxGuests;
    private Double latitude;
    private Double longitude;
    private List<PropertyImage> images;
    private List<Amenity> amenities;
    private List<AvailabilityEntry> availabilityEntries;
    private String type;
    private List<Review> reviews;
    private Double averageRating;
    private String pricingMode;
    private Integer cancellationDeadline;
    private boolean autoApproveEnabled;
    private List<Long> amenityIds;
    private Host host;

    public Property() {
        address = new Address("", "", "");
        minGuests = 1;
        maxGuests = 1;
        images = new ArrayList<>();
        amenities = new ArrayList<>();
        amenityIds = new ArrayList<>();
        availabilityEntries = new ArrayList<>();
        reviews = new ArrayList<>();
        pricingMode = "";
        latitude = null;
        longitude = null;
    }

    public Property(Long id, String name, String description, Address address, int minGuests, int maxGuests, Double latitude, Double longitude, List<PropertyImage> images, List<Amenity> amenities, List<AvailabilityEntry> availabilityEntries, String type, List<Review> reviews, Double averageRating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
        this.amenities = amenities;
        this.availabilityEntries = availabilityEntries;
        this.type = type;
        this.reviews = reviews;
        this.averageRating = averageRating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<PropertyImage> getImages() {
        return images;
    }

    public void setImages(List<PropertyImage> images) {
        this.images = images;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
        this.amenityIds = amenities.stream().map(amenity -> amenity.getId()).collect(Collectors.toList());
    }

    public List<AvailabilityEntry> getAvailabilityEntries() {
        return availabilityEntries;
    }

    public void setAvailabilityEntries(List<AvailabilityEntry> availabilityEntries) {
        this.availabilityEntries = availabilityEntries;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
    public void addAvailability(LocalDate beginDate, LocalDate endDate, Double price) {
        for (LocalDate date = beginDate; !date.isAfter(endDate); date = date.plusDays(1))
        {
            LocalDate finalDate = date;
            if (this.getAvailabilityEntries().stream().anyMatch(availabilityEntry -> availabilityEntry.getDate().equals(finalDate))) {
                AvailabilityEntry entryForDate = this.getAvailabilityEntries().stream().filter(availabilityEntry -> availabilityEntry.getDate().equals(finalDate)).findFirst().get();
                entryForDate.setPrice(price);
            } else {
                this.getAvailabilityEntries().add(new AvailabilityEntry(date, price));
            }
        }
    }

    public void deleteAvailability(LocalDate beginDate, LocalDate endDate) {
        this.availabilityEntries = this.availabilityEntries.stream()
                .filter(availabilityEntry ->
                        availabilityEntry.getDate().isBefore(beginDate) ||
                        availabilityEntry.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public String getPricingMode() {
        return pricingMode;
    }

    public void setPricingMode(String pricingMode) {
        this.pricingMode = pricingMode;
    }

    public Integer getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(Integer cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public boolean isAutoApproveEnabled() {
        return autoApproveEnabled;
    }

    public void setAutoApproveEnabled(boolean autoApproveEnabled) {
        this.autoApproveEnabled = autoApproveEnabled;
    }

    public List<Long> getAmenityIds() {
        return amenityIds;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address=" + address +
                ", minGuests=" + minGuests +
                ", maxGuests=" + maxGuests +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", images=" + images +
                ", amenities=" + amenities +
                ", availabilityEntries=" + availabilityEntries +
                ", type='" + type + '\'' +
                ", reviews=" + reviews +
                ", averageRating=" + averageRating +
                ", pricingMode='" + pricingMode + '\'' +
                ", cancellationDeadline=" + cancellationDeadline +
                ", autoApproveEnabled=" + autoApproveEnabled +
                ", amenityIds=" + amenityIds +
                ", host=" + host +
                '}';
    }
}
