package com.example.lunark.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Review {
    private Long id;
    private String author;
    private LocalDate date;
    private int rating;
    private String description;

    public Review() {
    }

    public Review(Long id, String author, LocalDate date, int rating, String description) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.rating = rating;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
