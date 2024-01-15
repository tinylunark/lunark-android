package com.example.lunark.models;

import java.util.Date;

public class Profile {
    private long id;
    private String email;
    private String name;
    private String surname;
    private String address;
    private String country;
    private String phoneNumber;
    private Date birthday;
    private ProfileImage profileImage;
    private double averageRating;
    private int cancelCount;
    private boolean verified;
    private boolean blocked;
    private GuestNotificationSettings guestNotificationSettings;
    private HostNotificationSettings hostNotificationSettings;

    public Profile() {
    }

    public Profile(long id, String email, String name, String surname, String address, String country, String phoneNumber, Date birthday, ProfileImage profileImage, double averageRating, int cancelCount, boolean verified, boolean blocked, GuestNotificationSettings guestNotificationSettings, HostNotificationSettings hostNotificationSettings) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.profileImage = profileImage;
        this.averageRating = averageRating;
        this.cancelCount = cancelCount;
        this.verified = verified;
        this.blocked = blocked;
        this.guestNotificationSettings = guestNotificationSettings;
        this.hostNotificationSettings = hostNotificationSettings;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(int cancelCount) {
        this.cancelCount = cancelCount;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public GuestNotificationSettings getGuestNotificationSettings() {
        return guestNotificationSettings;
    }

    public void setGuestNotificationSettings(GuestNotificationSettings guestNotificationSettings) {
        this.guestNotificationSettings = guestNotificationSettings;
    }

    public HostNotificationSettings getHostNotificationSettings() {
        return hostNotificationSettings;
    }

    public void setHostNotificationSettings(HostNotificationSettings hostNotificationSettings) {
        this.hostNotificationSettings = hostNotificationSettings;
    }
}