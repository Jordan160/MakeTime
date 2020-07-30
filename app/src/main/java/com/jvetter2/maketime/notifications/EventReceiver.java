package com.jvetter2.maketime.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.jvetter2.maketime.Data.EventAdapter;
import com.jvetter2.maketime.MainActivity;
import com.jvetter2.maketime.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class EventReceiver extends BroadcastReceiver {
    private int id;
    private int count = 0;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        id = intent.getIntExtra("notification_id", 100);
        boolean scheduleNotification = intent.getBooleanExtra("schedule_notification", false);
        boolean completeEvent = intent.getBooleanExtra("completed", false);
        String title = intent.getStringExtra("name");
        String duration = intent.getStringExtra("duration");

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (scheduleNotification) {
            RemoteViews notificationView = new RemoteViews(context.getPackageName(),
                    R.layout.notification_layout);

            DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
            String dateString2 = dateFormat.format(new Date());
            notificationView.setTextViewText(R.id.tvTimeOfAlert, dateString2);
            notificationView.setTextViewText(R.id.tvAlertTitle, title);
            notificationView.setTextViewText(R.id.tvDuration, duration);

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
            switchIntent.putExtra("notification_id", id);
            PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0,
                    switchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationView.setOnClickPendingIntent(R.id.dismissButton,
                    pendingSwitchIntent);

            Intent completeIntent = new Intent(context, EventReceiver.class);
            completeIntent.putExtra("completed", true);
            completeIntent.putExtra("notification_id", id);
            PendingIntent completeSwitchIntent = PendingIntent.getBroadcast(context, 0,
                    completeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationView.setOnClickPendingIntent(R.id.completeButton,
                    completeSwitchIntent);

            notificationManager.notify(id, notification);
        } else if (completeEvent){
            notificationManager.cancel(id);
            SQLiteDatabase myDatabase = context.openOrCreateDatabase("events", MODE_PRIVATE, null);
            EventAdapter.completeEvent(myDatabase, "true", String.valueOf(id));
            Intent mainIntent = new Intent(context, MainActivity.class);
            context.startActivity(mainIntent);

        } else {
            notificationManager.cancel(id);
        }
    }
}