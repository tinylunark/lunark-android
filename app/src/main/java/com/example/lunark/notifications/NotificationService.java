package com.example.lunark.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.lunark.BuildConfig;
import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.repositories.LoginRepository;

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
    private NotificationManager notificationManager;
    private NotificationChannel channel;

    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;
    @Inject
    LoginRepository loginRepository;

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
        createNotificationChannel();
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
                    Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();
                    break;
                case ACTION_STOP_NOTIFICATION_SERVICE:
                    Toast.makeText(getApplicationContext(), "You clicked Stop button.", Toast.LENGTH_LONG).show();
                    stopForeground(true);
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    /* Used to build and start foreground service. */
    private void startForegroundService()
    {
        Log.d(TAG_NOTIFICATION_SERVICE, "Start foreground service.");

        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationID, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        // Make notification show big text.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Music player implemented by foreground service.");
        bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always," +
                "it can be controlled by user via notification.");
        // Set big text style.
        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check);
        builder.setLargeIcon(largeIconBitmap);
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);

        connectStomp();
        // Start foreground service.
        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(notificationID, builder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING);
        } else {
            startForeground(notificationID, builder.build());
        }
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
                            toast("Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(TAG_NOTIFICATION_SERVICE, "Stomp connection error", lifecycleEvent.getException());
                            toast("Stomp connection error");
                            break;
                        case CLOSED:
                            toast("Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            toast("Stomp failed server heartbeat");
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

    private void toast(String text) {
        Log.i(TAG_NOTIFICATION_SERVICE, text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        CharSequence name = "Notification channel";
        String description = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStompClient.disconnect();
        if (compositeDisposable != null) compositeDisposable.dispose();

        Toast.makeText(getApplicationContext(), "Stop foreground service.", Toast.LENGTH_LONG).show();
        // Stop foreground service and remove the notification.
        stopForeground(true);
        stopSelf();
    }
}
