package com.example.lunark.dtos;

import com.example.lunark.models.GuestNotificationSettings;
import com.example.lunark.models.HostNotificationSettings;

public class AccountDto {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private String address;
    private String phoneNumber;
    private String role;
    private Boolean verified;
    private Boolean blocked;
    private GuestNotificationSettings guestNotificationSettings;
    private HostNotificationSettings hostNotificationSettings;

    public AccountDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getBlocked() {
        return blocked;
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

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }
}