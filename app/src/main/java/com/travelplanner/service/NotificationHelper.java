package com.travelplanner.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.travelplanner.R;
import com.travelplanner.ui.MainActivity;

public class NotificationHelper {
    private static final String CHANNEL_ID_REMINDER = "reminder_channel";
    private static final String CHANNEL_NAME_REMINDER = "行程提醒";
    private static final int NOTIFICATION_ID_BASE = 20000;

    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context.getApplicationContext();
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_REMINDER,
                    CHANNEL_NAME_REMINDER,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            channel.setDescription("行程相关提醒通知");
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showReminderNotification(Long reminderId, String title, String content, String type) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                reminderId.intValue(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String notificationTitle = title != null ? title : "行程提醒";
        String notificationContent = content != null ? content : "您有一个行程提醒";

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID_REMINDER)
                .setSmallIcon(R.drawable.ic_ticket)
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build();

        notificationManager.notify(NOTIFICATION_ID_BASE + reminderId.intValue(), notification);
    }

    public void cancelNotification(Long reminderId) {
        notificationManager.cancel(NOTIFICATION_ID_BASE + reminderId.intValue());
    }

    public void showSimpleNotification(String title, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID_REMINDER)
                .setSmallIcon(R.drawable.ic_ticket)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }
}
