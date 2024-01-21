package com.example.lunark.models;

import androidx.annotation.NonNull;

public enum SortOrder {
    ASC,
    DESC;

    @NonNull
    @Override
    public String toString() {
        if (this == SortOrder.DESC) {
            return "DESC";
        }
        return "ASC";
    }

    public SortOrder toggle() {
        if (this == SortOrder.DESC) {
            return SortOrder.ASC;
        }
        return SortOrder.DESC;
    }
}
