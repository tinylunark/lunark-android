package com.example.lunark.models;

public class Property {
    private String name;
    private String description;
    private String location;
    private double price;
    private int thumbnailId;

    public Property() {
    }

    public Property(String name, String description, String location, double price, int thumbnailId) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.price = price;
        this.thumbnailId = thumbnailId;
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

    @Override
    public String toString() {
        return "Accommodation{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
