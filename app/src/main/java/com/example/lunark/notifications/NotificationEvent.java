package com.example.lunark.notifications;

import com.example.lunark.models.Notification;

public class NotificationEvent {
    public Notification notification;

    public NotificationEvent(Notification notification) {
        this.notification = notification;
    }
}
