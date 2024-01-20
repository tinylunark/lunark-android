package com.example.lunark.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.lunark.BuildConfig;
import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.models.Notification;
import com.example.lunark.repositories.LoginRepository;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class NotificationService extends Service {

    private static final String TAG_NOTIFICATION_SERVICE = "NOTIFICATION_SERVICE";
    public static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    public static final String ACTION_STOP_NOTIFICATION_SERVICE = "ACTION_STOP_NOTIFICATION_SERVICE";
    private static final String CHANNEL_ID = "Zero channel";
    private static final String PERMANENT_NOTIFICATION_CHANNEL_ID = "Permanent channel";
    private NotificationManager notificationManager;
    private NotificationChannel channel;
    private NotificationChannel permanentNotificationChannel;

    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;
    @Inject
    LoginRepository loginRepository;

    @Inject
    Gson gson;

    private int notificationID = 50;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((LunarkApplication) getApplicationContext()).applicationComponent.inject(this);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + BuildConfig.IP_ADDR + ":8080/api/non-sockjs-socket");
        createNotificationChannels();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = getSystemService(NotificationManager.class);
        if (intent != null)
        {
            String action = intent.getAction();
            Log.i("SERVICE STARTED", "YES");
            switch (action)
            {
                case ACTION_START_NOTIFICATION_SERVICE:
                    startForegroundService();
                    break;
                case ACTION_STOP_NOTIFICATION_SERVICE:
                    stopForegroundService();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    private void startForegroundService()
    {
        Log.d(TAG_NOTIFICATION_SERVICE, "Start foreground service.");

        connectStomp();

        android.app.Notification permanentNotification = createPermanentNotification();

        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(notificationID, permanentNotification, ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING);
        } else {
            startForeground(notificationID, permanentNotification);
        }
    }

    private void stopForegroundService() {
        mStompClient.disconnect();
        if (compositeDisposable != null) compositeDisposable.dispose();
        stopForeground(true);
    }

    @NonNull
    private android.app.Notification createPermanentNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PERMANENT_NOTIFICATION_CHANNEL_ID);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Listening for notifications");
        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.lunark_icon);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check);
        builder.setLargeIcon(largeIconBitmap);
        builder.setPriority(NotificationCompat.PRIORITY_MIN);
        return builder.build();
    }

    public void connectStomp() {

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", "Bearer: " + loginRepository.getLogin().blockingGet().getAccessToken()));

        mStompClient.withClientHeartbeat(30000).withServerHeartbeat(1000);

        resetSubscriptions();

        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d(TAG_NOTIFICATION_SERVICE, "Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(TAG_NOTIFICATION_SERVICE, "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.d(TAG_NOTIFICATION_SERVICE, "Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.e(TAG_NOTIFICATION_SERVICE, "Stomp failed server heartbeat");
                            break;
                    }
                });

        compositeDisposable.add(dispLifecycle);

        // Receive greetings
        Disposable dispTopic = mStompClient.topic("/user/socket-publisher")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG_NOTIFICATION_SERVICE, "Received " + topicMessage.getPayload());
                    Notification notification = parseNotification(topicMessage.getPayload());
                    if (notification == null) {
                        Log.d(TAG_NOTIFICATION_SERVICE, "Received unread notification count or could not parse notification");
                    } else {
                        Log.d(TAG_NOTIFICATION_SERVICE, "Received proper notification with id " + notification.getId() + " and text \"" + notification.getText() + "\" which was sent on " + notification.getDate());
                        show(notification);
                    }
                }, throwable -> {
                    Log.e(TAG_NOTIFICATION_SERVICE, "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);

        mStompClient.connect(headers);
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    private void createNotificationChannels() {
        CharSequence name = "Notification channel";
        String description = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        permanentNotificationChannel = new NotificationChannel(PERMANENT_NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);

        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        notificationManager.createNotificationChannel(permanentNotificationChannel);
    }

    private void show(Notification notification) {
        EventBus.getDefault().post(new NotificationEvent(notification));
        Intent intent = new Intent(NotificationReceiver.PUSH_NOTIFICATION);
        intent.putExtra(NotificationReceiver.NOTIFICATION_KEY, notification);
        getApplicationContext().sendBroadcast(intent);
    }

    private Notification parseNotification(String json) {
        Notification notification = gson.fromJson(json, Notification.class);
        return notification.getId() != null ? notification : null;
    }
}
