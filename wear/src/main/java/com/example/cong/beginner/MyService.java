package com.example.cong.beginner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;


public class MyService extends Service implements SensorEventListener {
    static public Notification.Builder baseNotificationBuilder;
    static volatile int started = 0;

    static public double dataPointBuffer[][];
    static public volatile int dataPointBufferHead;
    static public volatile int dataPointBufferTail;
    static public int bufferSize = 10000;
    static Service myService;

    Thread worker;

    private SensorManager mSensorManager;

    public MyService() {
        myService = this;
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

        dataPointBuffer = new double[bufferSize][4];
        dataPointBufferHead = 0;
        dataPointBufferTail = 0;

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mLinearAcce = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        mSensorManager.registerListener(this, mLinearAcce, SensorManager.SENSOR_DELAY_GAME);

        startForeground(2, baseNotification);


        if (worker==null) {
            worker = new Thread(new BufferMonitor());
            worker.start();
        }
    }

    @Override
    public void onDestroy(){
        myService = null;
        mSensorManager.unregisterListener(this);
//        service.shutdown();
//        if (worker != null)
//            worker.interrupt();
//        wl.release();
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        dataPointBuffer[dataPointBufferHead][0] = event.timestamp;
        dataPointBufferHead++;
        if (dataPointBufferHead == dataPointBufferTail)
            throw new RuntimeException("Buffer overflow");
    }
}
