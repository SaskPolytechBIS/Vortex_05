package com.example.mscarenew.ui.medtrack;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mscarenew.R;

public class AlertReceiver extends BroadcastReceiver {
    private static final String TAG = "AlertReceiver";
    private static final String CHANNEL_ID = "reminder_channel";
    private static final String CHANNEL_NAME = "Medication Reminders";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive(): entered");
        String reminderMessage = intent.getStringExtra("EXTRA_REMINDER_MESSAGE");
        String notificationTitle = "MSCare Medication Reminder";

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for medication reminders");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_small_notification)
                .setContentTitle(notificationTitle)
                .setContentText(reminderMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
        Log.d(TAG, "Notification sent: " + reminderMessage);
    }
}
