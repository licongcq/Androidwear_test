package com.example.cong.beginner;

import android.app.Notification;
import android.app.NotificationManager;

/**
 * Created by Cong on 2015/4/24.
 */
public class BufferMonitor implements Runnable{
    @Override
    public void run() {
        while(!Thread.interrupted()) {
            int endHeader = MyService.dataPointBufferHead;
            if (MyService.dataPointBufferTail != MyService.dataPointBufferHead) {
                double startTime = MyService.dataPointBuffer[MyService.dataPointBufferTail][0];
                double endTime = startTime;
                int packNumber = 0;
                while (MyService.dataPointBufferTail != endHeader) {
                    packNumber++;
                    endTime = MyService.dataPointBuffer[MyService.dataPointBufferTail][0];
                    MyService.dataPointBufferTail = (MyService.dataPointBufferTail + 1) % MyService.bufferSize;
                }

                double timeGap = endTime - startTime;

                MyService.baseNotificationBuilder
                        .setContentTitle("Sensor status")
                        .setContentText(String.format("TimIntv: %f, Pack: %d", timeGap, packNumber));

                Notification baseNotification = MyService.baseNotificationBuilder.build();
                NotificationManager notificationManager = (NotificationManager) MyService.myService.getSystemService(MyService.NOTIFICATION_SERVICE);
                notificationManager.notify(2, baseNotification);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException expected){
                return;
            }
        }
    }
}
