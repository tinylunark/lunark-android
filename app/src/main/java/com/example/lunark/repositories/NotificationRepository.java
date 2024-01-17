package com.example.lunark.repositories;

import com.example.lunark.datasources.NotificationNetworkDataSource;
import com.example.lunark.models.Notification;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class NotificationRepository {
    private NotificationNetworkDataSource notificationNetworkDataSource;

    @Inject
    public NotificationRepository(NotificationNetworkDataSource notificationNetworkDataSource) {
        this.notificationNetworkDataSource = notificationNetworkDataSource;
    }

    public Single<List<Notification>> getNotifications() {
        return this.notificationNetworkDataSource.getNotifications();
    }
}
