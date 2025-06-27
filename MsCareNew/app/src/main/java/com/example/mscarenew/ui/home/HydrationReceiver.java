// HydrationReceiver.java
package com.example.mscarenew.ui.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;
import com.example.mscarenew.R;

public class HydrationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "SCareHydrationChannel";
    private static final int NOTIF_ID = 1001;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 1) Ensure channel exists
        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm.getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel chan = new NotificationChannel(
                    CHANNEL_ID,
                    "Hydration Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            chan.setDescription("Reminds you to drink water regularly");
            chan.enableLights(true);
            chan.setLightColor(Color.BLUE);
            nm.createNotificationChannel(chan);
        }

        // 2) Build notification
        NotificationCompat.Builder notif = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_water_drop)    // add a water-drop icon in drawable
                .setContentTitle("Hydration Reminder")
                .setContentText("Time to drink a glass of water 💧")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(context.getColor(R.color.teal_700));

        // 3) Optional: tap to open the app’s hydration screen
        Intent launch = new Intent(context, HomeFragment.class);
        PendingIntent pi = PendingIntent.getActivity(
                context, 0, launch,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        notif.setContentIntent(pi);

        // 4) Fire
        nm.notify(NOTIF_ID, notif.build());
    }
}
