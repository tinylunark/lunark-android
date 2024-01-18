package com.example.lunark.models;

public class GuestNotificationSettings {
    private boolean notifyOnReservationRequestResponse;

    public GuestNotificationSettings() {
    }

    public GuestNotificationSettings(boolean notifyOnReservationRequestResponse) {
        this.notifyOnReservationRequestResponse = notifyOnReservationRequestResponse;
    }

    public boolean isNotifyOnReservationRequestResponse() {
        return notifyOnReservationRequestResponse;
    }

    public void setNotifyOnReservationRequestResponse(boolean notifyOnReservationRequestResponse) {
        this.notifyOnReservationRequestResponse = notifyOnReservationRequestResponse;
    }
}