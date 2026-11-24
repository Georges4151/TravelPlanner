package com.travelplanner.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.travelplanner.model.Reminder;
import com.travelplanner.receiver.ReminderReceiver;

public class ReminderManager {
    private static final String TAG = "ReminderManager";
    private static final String ACTION_REMINDER = "com.travelplanner.REMINDER";

    private final Context context;
    private final AlarmManager alarmManager;

    public ReminderManager(Context context) {
        this.context = context.getApplicationContext();
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void scheduleReminder(Reminder reminder) {
        if (reminder == null || reminder.getId() == null || reminder.getRemindTime() == null) {
            return;
        }

        long triggerTime = reminder.getRemindTime();
        if (triggerTime <= System.currentTimeMillis()) {
            return;
        }

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction(ACTION_REMINDER);
        intent.putExtra("reminder_id", reminder.getId());
        intent.putExtra("title", reminder.getTitle());
        intent.putExtra("content", reminder.getContent());
        intent.putExtra("type", reminder.getType());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.getId().intValue(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent);
        } else {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent);
        }

        Log.d(TAG, "Scheduled reminder: " + reminder.getId() + " at " + triggerTime);
    }

    public void cancelReminder(Long reminderId) {
        if (reminderId == null) {
            return;
        }

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction(ACTION_REMINDER);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                reminderId.intValue(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
        Log.d(TAG, "Cancelled reminder: " + reminderId);
    }

    public void updateReminder(Reminder reminder) {
        cancelReminder(reminder.getId());
        scheduleReminder(reminder);
    }

    public void scheduleAllReminders(java.util.List<Reminder> reminders) {
        if (reminders == null)
            return;
        for (Reminder reminder : reminders) {
            scheduleReminder(reminder);
        }
    }
}
