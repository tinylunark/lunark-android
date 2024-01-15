package com.example.lunark.datasources;

import android.util.Log;

import com.example.lunark.models.Login;
import com.example.lunark.models.Notification;
import com.example.lunark.services.NotificationService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class NotificationNetworkDataSource {
    public static NotificationService notificationService;

    @Inject
    public NotificationNetworkDataSource(Retrofit retrofit) {
       notificationService = retrofit.create(NotificationService.class);
    }

    public Single<List<Notification>> getNotifications() {
       Single<List<Notification>> call = notificationService.getNotifications();
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Notification>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Notification> notifications) {
                        Log.d("NOTIFICATIONS", "Successfully fetched notifications from server");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NOTIFICATIONS", "Error while fetching notifications\n" + e.getMessage());
                    }
                });
        return call;
    }
}
