package com.jvetter2.maketime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    Button saveButton;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    RadioButton englishButton;
    RadioButton spanishButton;
    RadioButton frenchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences preferences =
                getSharedPreferences("my_preferences", MODE_PRIVATE);
        String language = preferences.getString("language", "default");

        if (!language.equalsIgnoreCase("default")) {
            LanguageHelper.updateLanguage(language, this);
        }

        setContentView(R.layout.activity_settings);

        radioGroup = findViewById(R.id.radioGroup);
        saveButton = findViewById(R.id.saveButton);
        englishButton = findViewById(R.id.englishButton);
        spanishButton = findViewById(R.id.spanishButton);
        frenchButton = findViewById(R.id.frenchButton);

        setTitle(R.string.action_settings);
        setCheckBox(language);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                selectedRadioButton = (RadioButton)findViewById(selectedId);
                if (selectedRadioButton.getText().toString().equalsIgnoreCase(getString(R.string.language_english))) {
                    preferences.edit().putString("language", "us").apply();
                } else if (selectedRadioButton.getText().toString().equalsIgnoreCase(getString(R.string.language_french))) {
                    preferences.edit().putString("language", "fr").apply();
                } else if (selectedRadioButton.getText().toString().equalsIgnoreCase(getString(R.string.language_spanish))) {
                    preferences.edit().putString("language", "es").apply();
                }
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private void setCheckBox(String language) {
        if (!language.equalsIgnoreCase("default")) {
            if (language.equalsIgnoreCase("us")) {
                englishButton.setChecked(true);
            } else if (language.equalsIgnoreCase("es")) {
                spanishButton.setChecked(true);
            } else if (language.equalsIgnoreCase("fr")) {
                frenchButton.setChecked(true);
            }
        }
    }
}
