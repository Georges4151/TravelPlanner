package com.travelplanner.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.travelplanner.service.NotificationHelper;
import com.travelplanner.service.ReminderManager;

public class ReminderReceiver extends BroadcastReceiver {
    private static final String TAG = "ReminderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received reminder broadcast");

        Long reminderId = intent.getLongExtra("reminder_id", 0);
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String type = intent.getStringExtra("type");

        if (reminderId == 0) {
            return;
        }

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.showReminderNotification(reminderId, title, content, type);

        // TODO: 更新提醒状态为已触发
    }
}
