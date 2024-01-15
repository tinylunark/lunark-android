package com.example.lunark.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Notification {
    private Long id;
    private String text;
    private String type;
    private ZonedDateTime date;
    private boolean read;

    public Notification(Long id, String text, String type, ZonedDateTime date) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.date = date;
        this.read = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
