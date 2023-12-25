package com.example.lunark.models;

import java.util.List;

public class Property {
    private Long id;
    private String name;
    private String description;
    private int minGuests;
    private int maxGuests;
    private Address address;
    private PropertyType type;
    private List<Amenity> amenities;
    private double longitude;
    private double latitude;
    private List<PropertyImage> images;

    public Property(Long id, String name, String description, int minGuests, int maxGuests, Address address, PropertyType type, List<Amenity> amenities, double longitude, double latitude, List<PropertyImage> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.address = address;
        this.type = type;
        this.amenities = amenities;
        this.longitude = longitude;
        this.latitude = latitude;
        this.images = images;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    public List<PropertyImage> getImages() {
        return images;
    }

    public void setImages(List<PropertyImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", minGuests=" + minGuests +
                ", maxGuests=" + maxGuests +
                ", address=" + address +
                ", type=" + type +
                ", amenities=" + amenities +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", images=" + images +
                '}';
    }

    public enum PropertyType {
        HUT,
        APARTMENT
    }
}
