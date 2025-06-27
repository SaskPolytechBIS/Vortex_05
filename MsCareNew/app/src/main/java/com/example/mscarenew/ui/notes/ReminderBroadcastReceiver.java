package com.example.mscarenew.ui.notes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mscarenew.R;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    public static final String EXTRA_REMINDER_TITLE   = "EXTRA_REMINDER_TITLE";
    public static final String EXTRA_REMINDER_MESSAGE = "EXTRA_REMINDER_MESSAGE";
    private static final String CHANNEL_ID = "MSCareChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title   = intent.getStringExtra(EXTRA_REMINDER_TITLE);
        String message = intent.getStringExtra(EXTRA_REMINDER_MESSAGE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_small_notification)
                .setContentTitle(title != null ? title : "MSCare Reminder")
                .setContentText(message != null ? message : "")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context) .notify((int) System.currentTimeMillis(), builder.build());
    }
}
