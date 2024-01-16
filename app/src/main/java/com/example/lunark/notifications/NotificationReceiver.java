package com.example.lunark.notifications;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.lunark.R;
import com.example.lunark.models.Notification;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {
    public static String PUSH_NOTIFICATION = "PUSH_NOTIFICATION";
    public static String NOTIFICATION_KEY = "NOTIFICATION";
    private static final String CHANNEL_ID = "Zero channel";
    public boolean isPermissions = false;
    private final String[] permissions = {Manifest.permission.POST_NOTIFICATIONS};

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("REZ", "onReceive");
        if (intent.getAction().equals(PUSH_NOTIFICATION)) {
            Notification notification = intent.getParcelableExtra(NOTIFICATION_KEY);
            show(notification, context);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("Lunark", "permission " + permissions[i] + " " + grantResults[i]);
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    isPermissions = false;
                }
            }
        }

        if (!isPermissions) {
            Log.e("Lunark", "Error: no permission");
        }

    }

    private void show(Notification notification, Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        setIcons(mBuilder, notification.getType(), context);
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
        mBuilder.setContentTitle(titles.get(notification.getType()))
                .setContentText(notification.getText())
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE));

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, permissions, 101);
        }
        else {
            notificationManager.notify(notificationIds.get(notification.getType()), mBuilder.build());
        }
    }

    private void setIcons(NotificationCompat.Builder mBuilder, String type, Context context) {
        if (!iconResourceIds.containsKey(type)) {
            throw new IllegalArgumentException("Invalid notification type");
        }
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), iconResourceIds.get(type));
        mBuilder.setLargeIcon(bm);
        mBuilder.setSmallIcon(iconResourceIds.get(type));
    }

    public static final HashMap<String, Integer> iconResourceIds = new HashMap<>();
    public static final HashMap<String, Integer> notificationIds = new HashMap<>();
    public static final HashMap<String, String> titles = new HashMap<>();

    static {
        iconResourceIds.put("PROPERTY_REVIEW", R.drawable.ic_grade);
        iconResourceIds.put("HOST_REVIEW", R.drawable.ic_host_review);
        iconResourceIds.put("RESERVATION_CREATED", R.drawable.ic_reservation);
        iconResourceIds.put("RESERVATION_CANCELED", R.drawable.ic_cancel);
        iconResourceIds.put("RESERVATION_ACCEPTED", R.drawable.ic_check);
        iconResourceIds.put("RESERVATION_REJECTED", R.drawable.ic_block);

        titles.put("PROPERTY_REVIEW", "New property review");
        titles.put("HOST_REVIEW", "New host review");
        titles.put("RESERVATION_CREATED", "You have received a new reservation request");
        titles.put("RESERVATION_CANCELED", "One of your reservations has been cancelled");
        titles.put("RESERVATION_ACCEPTED", "Reservation accepted");
        titles.put("RESERVATION_REJECTED", "Reservation rejected");

        List<String> types = Arrays.asList("PROPERTY_REVIEW", "HOST_REVIEW", "RESERVATION_CREATED", "RESERVATION_CANCELED", "RESERVATION_ACCEPTED", "RESERVATION_REJECTED");
        for (int i = 0; i < types.size(); i++) {
            notificationIds.put(types.get(i), i+1);
        }
    }

}
