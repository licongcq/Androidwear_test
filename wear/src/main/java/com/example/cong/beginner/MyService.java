package com.example.cong.beginner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;

public class MyService extends Service {
    static public Notification.Builder baseNotificationBuilder;
    static volatile int started = 0;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
         Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .extend(new Notification.WearableExtender()
                        .setDisplayIntent(notificationPendingIntent)
                        .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN))
                .setSmallIcon(R.drawable.common_signin_btn_icon_focus_dark)
                .setContentText("Test Content Text")
                .setContentTitle("Test Content Title")
                .build();

        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        baseNotificationBuilder = new Notification.Builder(this)
                .extend(new Notification.WearableExtender()
                        .addPage(notification))
                .setSmallIcon(R.drawable.common_signin_btn_icon_focus_dark)
                .setContentText("Test Content Text Base")
                .setContentTitle("Test Content Title")
                .setContentIntent(mainPendingIntent);

        Notification baseNotification =  baseNotificationBuilder.build();

        startForeground(2, baseNotification);
    }
}
