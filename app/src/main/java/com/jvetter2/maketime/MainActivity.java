package com.jvetter2.maketime;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.jvetter2.maketime.fragments.AddEditFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FloatingActionButton fab;
    FragmentManager mFragmentManager;
    NotificationManager notificationManager;

    public static ArrayList<String> eventNames = new ArrayList();
    public static ArrayList<String> eventTimeOfDay = new ArrayList();
    public static ArrayList<String> eventDate = new ArrayList();
    public static ArrayList<String> eventDuration = new ArrayList();
    public static ArrayList<Boolean> eventStatus = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseAuth.getInstance().signOut();
        setLanguage();
        setTitle(R.string.app_name);

        mFragmentManager = this.getSupportFragmentManager();

        initializeDatabase();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.hide();
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.addToBackStack(AddEditFragment.TAG);
                ft.replace(R.id.fragment_home, new AddEditFragment(), AddEditFragment.TAG);
                ft.commit();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.sign_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //sendNotification();
        //startNotification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStackImmediate();
            if (fab.isOrWillBeHidden()) {
                fab.show();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void initializeDatabase() {
        eventNames.clear();
        eventTimeOfDay.clear();
        eventDate.clear();
        eventDuration.clear();
        eventStatus.clear();

        SQLiteDatabase myDatabase = this.openOrCreateDatabase("events", MODE_PRIVATE, null);

        try {
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS events (name VARCHAR, time VARCHAR, " +
                    "date VARCHAR, duration VARCHAR, status VARCHAR)");

            //myDatabase.execSQL("INSERT INTO events VALUES('SkyHarp', 'Morning', '01/01/2020', '30 Minutes', 'false');");

            Cursor c = myDatabase.rawQuery("SELECT * FROM events ORDER BY date COLLATE NOCASE ASC", null);

            int nameIndex = c.getColumnIndex("name");
            int timeIndex = c.getColumnIndex("time");
            int dateIndex = c.getColumnIndex("date");
            int durationIndex = c.getColumnIndex("duration");
            int statusIndex = c.getColumnIndex("status");

            c.moveToFirst();

            while (!c.isAfterLast()) {
                Log.i("name: ", c.getString(nameIndex));
                Log.i("time: ", c.getString(timeIndex));
                Log.i("date: ", c.getString(dateIndex));
                Log.i("duration: ", c.getString(durationIndex));
                Log.i("status: ", c.getString(statusIndex));

                eventNames.add(c.getString(nameIndex));
                eventTimeOfDay.add(c.getString(timeIndex));
                eventDate.add(c.getString(dateIndex));
                eventDuration.add(c.getString(durationIndex));
                eventStatus.add(Boolean.valueOf(c.getString(statusIndex)));

                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLanguage() {
        SharedPreferences preferences =
                getSharedPreferences("my_preferences", MODE_PRIVATE);
        String language = preferences.getString("language", "default");
        if (!language.equalsIgnoreCase("default")) {
            LanguageHelper.updateLanguage(language, this);
        }
    }
}





//318027238525-l6t8915c5pe9ab8m5hejiba8rb714l77.apps.googleusercontent.com
//QpNgl0vCnshxr4GPIbWv2ayA



//756979620471-kgfgjra2ip8l88v4vqsr2vgs8hritv9g.apps.googleusercontent.com
//eQm58jFEEeWxCIjLO5m-hYfo