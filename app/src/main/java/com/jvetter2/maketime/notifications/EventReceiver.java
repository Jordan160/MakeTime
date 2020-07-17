package com.jvetter2.maketime.notifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EventReceiver extends BroadcastReceiver {
    private int id;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Received Cancelled Event");
        id = intent.getIntExtra("notification_id", 100);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}