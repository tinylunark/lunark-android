package com.example.lunark.models;

public class Property {
    private String name;
    private String description;
    private String location;
    private double price;
    private int thumbnailId;
    private double averageRating;
    private PropertyType type;

    public Property() {
    }

    public Property(String name, String description, String location, double price, int thumbnailId, double averageRating, PropertyType type) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.price = price;
        this.thumbnailId = thumbnailId;
        this.averageRating = averageRating;
        this.type = type;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(int thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public enum PropertyType {
        HUT,
        APARTMENT
    }
}
