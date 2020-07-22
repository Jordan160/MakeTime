package com.jvetter2.maketime.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.jvetter2.maketime.MainActivity;
import com.jvetter2.maketime.R;

public class EventReceiver extends BroadcastReceiver {
    private int id;
    private int count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Received Cancelled Event");
        id = intent.getIntExtra("notification_id", 100);
        String dismiss = intent.getStringExtra("removeAlert");

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (dismiss != null && dismiss.equalsIgnoreCase("sure") && id == 300) {
            notificationManager.cancel(300);
            notificationManager.cancel(0);
        }

        else {
            RemoteViews notificationView = new RemoteViews(context.getPackageName(),
                    R.layout.notification_layout);
            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "meatball")
                    .setSmallIcon(R.raw.time_icon)
                    .setContent(contentView);

            Notification notification = mBuilder.build();
            mBuilder.setCustomContentView(contentView);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);

            notification.contentView = notificationView;
            notification.contentIntent = pendingNotificationIntent;
            notification.flags |= Notification.FLAG_NO_CLEAR;

            Intent switchIntent = new Intent(context, EventReceiver.class);
            switchIntent.putExtra("removeAlert", "Sure");
            switchIntent.putExtra("notification_id", 300);
            PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0,
                    switchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationView.setOnClickPendingIntent(R.id.dismissButton,
                    pendingSwitchIntent);

            notificationManager.notify(300, notification);
        }
    }
}