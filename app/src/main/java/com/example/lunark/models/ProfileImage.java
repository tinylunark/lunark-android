package com.example.lunark.models;

public class ProfileImage {
    private byte[] imageData;
    private String mimeType;


    public ProfileImage() {
    }

    public ProfileImage(byte[] imageData, String mimeType) {
        this.imageData = imageData;
        this.mimeType = mimeType;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
