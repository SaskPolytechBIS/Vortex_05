package com.example.mscarealpha.ui.medtrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.example.mscarealpha.R;

public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderType = intent.getStringExtra("EXTRA_REMINDER_TYPE");
        String reminderMessage = intent.getStringExtra("EXTRA_REMINDER_MESSAGE");
        Log.d(TAG, "Received reminder: " + reminderMessage);

        if (reminderType != null && reminderMessage != null) {
            createNotification(context, reminderType, reminderMessage);
        }
    }

    private void createNotification(Context context, String reminderType, String reminderMessage) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "reminder_channel";
        String channelName = "Reminder Notifications";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for Reminder Notifications");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                Log.d(TAG, "Notification channel created");
            } else {
                Log.e(TAG, "NotificationManager is null");
            }
        }

        String notificationTitle = reminderType.equals("MEDICATION") ? "Medication Reminder" : "Appointment Reminder";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.icon_small_notification)  // Ensure you have this icon in your drawables
                .setContentTitle(notificationTitle)
                .setContentText(reminderMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
            Log.d(TAG, "Notification sent");
        } else {
            Log.e(TAG, "NotificationManager is null");
        }
    }
}
