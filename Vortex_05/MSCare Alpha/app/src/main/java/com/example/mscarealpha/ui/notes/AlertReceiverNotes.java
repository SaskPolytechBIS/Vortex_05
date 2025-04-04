package com.example.mscarealpha.ui.notes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.example.mscarealpha.R;

public class AlertReceiverNotes extends BroadcastReceiver {

    private static final String TAG = "AlertReceiverNotes";

    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderTitle = intent.getStringExtra("EXTRA_REMINDER_TITLE");
        String reminderMessage = intent.getStringExtra("EXTRA_REMINDER_MESSAGE");
        Log.d(TAG, "Received reminder: " + reminderTitle + " - " + reminderMessage);
        createNotification(context, reminderTitle, reminderMessage);
    }

    private void createNotification(Context context, String reminderTitle, String reminderMessage) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "appointment_reminder",
                    "Appointment Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for Appointment Reminder");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                Log.d(TAG, "Notification channel created");
            } else {
                Log.e(TAG, "NotificationManager is null");
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "appointment_reminder")
                .setSmallIcon(R.drawable.icon_small_notification)  // Ensure you have this icon in your drawables
                .setContentTitle("Appointment Reminder: " + reminderTitle)
                .setContentText(reminderMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (notificationManager != null) {
            notificationManager.notify(2, builder.build());
            Log.d(TAG, "Notification sent");
        } else {
            Log.e(TAG, "NotificationManager is null");
        }
    }
}

