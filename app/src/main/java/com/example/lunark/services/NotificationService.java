package com.example.lunark.services;

import com.example.lunark.models.Notification;
import com.example.lunark.models.Property;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface NotificationService {
    @GET("notifications")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Single<List<Notification>> getNotifications();
}
