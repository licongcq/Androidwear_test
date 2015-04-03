package com.example.cong.beginner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class MyService extends Service {
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
                        .setCustomSizePreset(Notification.WearableExtender.SIZE_MEDIUM))
                .build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//        Notification.Builder builder = new Notification.Builder(this)
//                .setContentTitle(getString(R.string.test_notification))
//                .setContentText(getString(R.string.test_notification))
//                .setContentIntent(pendingIntent);
//
//        int mId = 1;
//        startForeground( mId, builder.build());
    }
}
