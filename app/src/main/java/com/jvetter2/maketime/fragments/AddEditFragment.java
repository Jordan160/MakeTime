package com.jvetter2.maketime.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jvetter2.maketime.MainActivity;
import com.jvetter2.maketime.R;
import com.jvetter2.maketime.notifications.EventReceiver;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class AddEditFragment extends Fragment {
    public static final String TAG = "AddEditFragment";
    int idNumber;
    EditText nameET;
    EditText dateET;
    EditText timeET;
    private String dateFormatted;
    NumberPicker np;
    NumberPicker typePicker;
    String[] arrayPicker= new String[]{"Minutes","Hours"};
    FloatingActionButton save;
    String[] timeOfDayCategories;


    public AddEditFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        np = view.findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(60);

        typePicker = view.findViewById(R.id.typePicker);
        typePicker.setMinValue(0);
        typePicker.setMaxValue(1);
        typePicker.setDisplayedValues(arrayPicker);

        Resources res = getResources();
        timeOfDayCategories = res.getStringArray(R.array.time_of_day);
        timeET = view.findViewById(R.id.ETTime);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, timeOfDayCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final SQLiteDatabase eventDatabase = getActivity().openOrCreateDatabase("events", MODE_PRIVATE, null);
        nameET = view.findViewById(R.id.nameET);
        save = view.findViewById(R.id.saveFloatingActionButton);

        final Calendar myCalendar = Calendar.getInstance();
        dateET = view.findViewById(R.id.dateET);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }
        };

        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);

        timeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String am_pm = "";
                                dateFormatted = (hourOfDay + ":" + minute);
                                if (c.get(Calendar.AM_PM) == Calendar.AM)
                                    am_pm = "AM";
                                else if (c.get(Calendar.AM_PM) == Calendar.PM)
                                    am_pm = "PM";

                                String strHrsToShow = (c.get(Calendar.HOUR) == 0) ?"12":c.get(Calendar.HOUR)+"";
                                if (Calendar.MINUTE < 10) {
                                    timeET.setText(strHrsToShow+":0"+c.get(Calendar.MINUTE)+" "+am_pm);
                                } else {
                                    timeET.setText(strHrsToShow+":"+c.get(Calendar.MINUTE)+" "+am_pm);
                                }
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        dateET = view.findViewById(R.id.dateET);
        dateET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        idNumber = (int) getIdNumber(eventDatabase);
        saveNewEvent(eventDatabase);
    }


    private void saveNewEvent(final SQLiteDatabase eventDatabase) {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    ContentValues cv = new ContentValues();
                    cv.put("name", nameET.getText().toString());
                    cv.put("time", timeET.getText().toString());
                    cv.put("date", dateET.getText().toString());
                    cv.put("duration", np.getValue() + " " + arrayPicker[typePicker.getValue()]);
                    cv.put("status", "false");
                    cv.put("id", idNumber);

                    scheduleNotification(getContext(), idNumber, nameET.getText().toString(), np.getValue() + " " + arrayPicker[typePicker.getValue()]);

                    try {
                        long count = eventDatabase.insert(
                                "events",
                                null,
                                cv);
                        Log.i("Count: ", String.valueOf(count));
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public long getIdNumber(SQLiteDatabase db) {
        long count = DatabaseUtils.queryNumEntries(db, "events");
        return count;
    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateET.setText(sdf.format(myCalendar.getTime()));
    }

    private Boolean validateFields() {
        if(nameET.getText().toString().isEmpty()) {
            nameET.setError(getString(R.string.event_name_validation));
            return false;
        }

        if(dateET.getText().toString().isEmpty()) {
            dateET.setError(getString(R.string.event_date_validation));
            return false;
        }
        return true;
    }

    private void scheduleNotification(Context context, int notificationID, String name, String duration) {
        Intent scheduleIntent = new Intent(context, EventReceiver.class);
        scheduleIntent.putExtra("schedule_notification", true);
        scheduleIntent.putExtra("notification_id", notificationID);
        scheduleIntent.putExtra("name", name);
        scheduleIntent.putExtra("duration", duration);
        PendingIntent scheduledIntent = PendingIntent.getBroadcast(context, notificationID,
                scheduleIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String date = dateET.getText().toString();
        String startDateString = date;
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        startDateString = startDateString.concat(" " + dateFormatted + ":00");


        Date startDate = null;
        try {
            startDate = df.parse(startDateString);
            String newDateString = df.format(startDate);
            System.out.println(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        long futureInMillis = startDate.getTime();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, scheduledIntent);
    }
}
