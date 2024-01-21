package com.example.lunark.models;

import androidx.annotation.NonNull;

public enum ReviewType {
    PROPERTY("PROPERTY"), HOST("HOST");
    private String value;
    ReviewType(String value) {
        this.value = value;
    }
    public static ReviewType fromString(String value) {
        switch (value) {
            case "PROPERTY":
                return ReviewType.PROPERTY;
            case "HOST":
                return ReviewType.HOST;
        }
        throw new IllegalArgumentException("Invalid string in account role");
    }
    @NonNull
    @Override
    public String toString() {
        return this.value;
    }
}
