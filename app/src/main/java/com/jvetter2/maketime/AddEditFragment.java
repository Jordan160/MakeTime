package com.jvetter2.maketime;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditFragment extends Fragment {
    public static final String TAG = "AddEditFragment";
    EditText nameET;
    EditText durationET;
    EditText dateET;
    EditText timeET;
    Spinner categorySpinner;


    FloatingActionButton save;
    String[] timeOfDayCategories;


    public AddEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Resources res = getResources();
        timeOfDayCategories = res.getStringArray(R.array.time_of_day);
        categorySpinner = view.findViewById(R.id.recipeCategorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, timeOfDayCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setPadding(0, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        categorySpinner.setAdapter(adapter);

        final SQLiteDatabase eventDatabase = getActivity().openOrCreateDatabase("events", MODE_PRIVATE, null);
        nameET = view.findViewById(R.id.nameET);
        durationET = view.findViewById(R.id.durationET);
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



        dateET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        saveNewEvent(eventDatabase);
    }


    private void saveNewEvent(final SQLiteDatabase eventDatabase) {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    ContentValues cv = new ContentValues();
                    cv.put("name", nameET.getText().toString());
                    cv.put("time", categorySpinner.getSelectedItem().toString());
                    cv.put("date", dateET.getText().toString());
                    cv.put("duration", durationET.getText().toString());
                    cv.put("status", "false");

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

        if(categorySpinner.getSelectedItem().toString().equalsIgnoreCase(timeOfDayCategories[0])) {
            TextView errorText = (TextView)categorySpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText(getString(R.string.event_time_of_day_validation));
            return false;
        }

        if(durationET.getText().toString().isEmpty()) {
            durationET.setError(getString(R.string.event_duration_validation));
            return false;
        }

        if(dateET.getText().toString().isEmpty()) {
            dateET.setError(getString(R.string.event_date_validation));
            return false;
        }
        return true;
    }
}
