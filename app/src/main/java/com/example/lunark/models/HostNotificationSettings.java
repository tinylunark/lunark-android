package com.example.lunark.models;

public class HostNotificationSettings {
    private boolean notifyOnReservationCreation;
    private boolean notifyOnReservationCancellation;
    private boolean notifyOnHostReview;
    private boolean notifyOnPropertyReview;

    public HostNotificationSettings() {
    }

    public HostNotificationSettings(boolean notifyOnReservationCreation, boolean notifyOnReservationCancellation, boolean notifyOnHostReview, boolean notifyOnPropertyReview) {
        this.notifyOnReservationCreation = notifyOnReservationCreation;
        this.notifyOnReservationCancellation = notifyOnReservationCancellation;
        this.notifyOnHostReview = notifyOnHostReview;
        this.notifyOnPropertyReview = notifyOnPropertyReview;
    }


    public boolean isNotifyOnReservationCreation() {
        return notifyOnReservationCreation;
    }

    public void setNotifyOnReservationCreation(boolean notifyOnReservationCreation) {
        this.notifyOnReservationCreation = notifyOnReservationCreation;
    }

    public boolean isNotifyOnReservationCancellation() {
        return notifyOnReservationCancellation;
    }

    public void setNotifyOnReservationCancellation(boolean notifyOnReservationCancellation) {
        this.notifyOnReservationCancellation = notifyOnReservationCancellation;
    }

    public boolean isNotifyOnHostReview() {
        return notifyOnHostReview;
    }

    public void setNotifyOnHostReview(boolean notifyOnHostReview) {
        this.notifyOnHostReview = notifyOnHostReview;
    }

    public boolean isNotifyOnPropertyReview() {
        return notifyOnPropertyReview;
    }

    public void setNotifyOnPropertyReview(boolean notifyOnPropertyReview) {
        this.notifyOnPropertyReview = notifyOnPropertyReview;
    }
}