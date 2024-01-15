package com.example.lunark.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Notification implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(text);
        dest.writeString(type);
        dest.writeString(date.toString());
        dest.writeInt(read ? 1 : 0);
    }

    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    private Notification(Parcel in) {
        id = in.readLong();
        text = in.readString();
        type = in.readString();
        date = ZonedDateTime.parse(in.readString());
        read = (in.readInt() == 1);
    }
}
