package com.example.cong.beginner;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }

    public void startService(View view) {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }
    public void stopService(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }
    public void issueNoti(View view) {
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

        Notification baseNotification =  new Notification.Builder(this)
                .extend(new Notification.WearableExtender()
                        .addPage(notification))
                .setSmallIcon(R.drawable.common_signin_btn_icon_focus_dark)
//                .setContentText("Test Content Text Base")
//                .setContentTitle("Test Content Title")
                .build();
/*        smallIcon is a must to issue a notification, content text and title are not*/

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, baseNotification);

        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText("Button hit");
    }
}
