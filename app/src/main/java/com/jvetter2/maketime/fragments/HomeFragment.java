package com.jvetter2.maketime.fragments;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jvetter2.maketime.Data.Event;
import com.jvetter2.maketime.Data.EventAdapter;
import com.jvetter2.maketime.Data.HomeViewModel;
import com.jvetter2.maketime.MainActivity;
import com.jvetter2.maketime.R;
import com.jvetter2.maketime.notifications.EventReceiver;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements EventAdapter.ItemClicked {

    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Event> event;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        event = new ArrayList<Event>();
        //event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));

        for(int i = 0; i < MainActivity.eventNames.size(); i++) {
            event.add(new Event(MainActivity.eventNames.get(i), MainActivity.eventDuration.get(i),
                    MainActivity.eventTimeOfDay.get(i), MainActivity.eventDate.get(i), MainActivity.eventStatus.get(i)));
        }

        myAdapter = new EventAdapter(this, event);

        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();


        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startNotification();
    }

    @Override
    public void onItemClicked(int index) {
         //Toast.makeText(getContext(), "Surname: " + event.get(index).getEventName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        startNotification();
        //EventReceiver.setDelayedNotification(getContext(), 200);

    }

    private void startNotification() {
//        String ns = Context.NOTIFICATION_SERVICE;
//        NotificationManager notificationManager =
//                (NotificationManager) getContext().getSystemService(ns);
//
////        notificationManager =
////                (NotificationManager)
////                        getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel notificationChannel = new NotificationChannel("meatball", "Your Notifications",
//                NotificationManager.IMPORTANCE_HIGH);
//
//        if (notificationManager != null) {
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        RemoteViews notificationView = new RemoteViews(getContext().getPackageName(),
//                R.layout.notification_layout);
//
//        RemoteViews contentView = new RemoteViews(getContext().getPackageName(), R.layout.notification_layout);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), "meatball")
//                .setSmallIcon(R.raw.time_icon)
//                .setContent(contentView);
//
//        Notification notification = mBuilder.build();
//        mBuilder.setCustomContentView(contentView);
//
//        //the intent that is started when the notification is clicked (works)
//        Intent notificationIntent = new Intent(getContext(), MainActivity.class);
//        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getContext(), 0,
//                notificationIntent, 0);
//
//        notification.contentView = notificationView;
//        notification.contentIntent = pendingNotificationIntent;
//        notification.flags |= Notification.FLAG_NO_CLEAR;
//
//        //this is the intent that is supposed to be called when the
//        //button is clicked
//
//        Intent switchIntent = new Intent(getContext(), EventReceiver.class);
//        switchIntent.putExtra("dismiss", true);
//        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(getContext(), 0,
//                switchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        notificationView.setOnClickPendingIntent(R.id.dismissButton,
//                pendingSwitchIntent);


        Intent scheduleIntent = new Intent(getContext(), EventReceiver.class);
        scheduleIntent.putExtra("notification_id", 0);
        PendingIntent scheduledIntent = PendingIntent.getBroadcast(getContext(), 0,
                scheduleIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + 1000;
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, scheduledIntent);

        //notificationManager.notify(100, notification);
    }

}