package com.example.lunark.models;

public class PropertyImage {
    private Long id;
    private String mimeType;

    public PropertyImage(Long id, String mimeType) {
        this.id = id;
        this.mimeType = mimeType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
