package com.example.javastart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

public class Menu extends AppCompatActivity {

    private Spinner spinnerEasy, spinnerMedium, spinnerHard;
    private Button nextButton, logoutButton;
    private String selectedTopic = "";
    private Set<String> completedTopics = new HashSet<>();
    private ImageView profileIcon;
    private TextView welcomeText;
    private String userRole = "user"; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        spinnerEasy = findViewById(R.id.spinner_easy);
        spinnerMedium = findViewById(R.id.spinner_medium);
        spinnerHard = findViewById(R.id.spinner_hard);
        nextButton = findViewById(R.id.button2);
        profileIcon = findViewById(R.id.profile_icon);
        logoutButton = findViewById(R.id.button5);
        // Load session info
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String name = sp.getString("name", "Guest");
        userRole = sp.getString("role", "user");

        // Load completed topics for this user
        completedTopics = new HashSet<>(sp.getStringSet("FinishedCourses", new HashSet<>()));

        setupSpinner(spinnerEasy, "Easy", new String[]{"What is Java"});
        setupSpinner(spinnerMedium, "Medium", new String[]{"Data Types"});
        setupSpinner(spinnerHard, "Hard", new String[]{"Array and ArrayList"});

        setSpinnerListener(spinnerEasy);
        setSpinnerListener(spinnerMedium);
        setSpinnerListener(spinnerHard);

        nextButton.setEnabled(false);
        nextButton.setOnClickListener(v -> handleNextButtonClick());

        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, Profile.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            // Clear session on logout
            sp.edit().clear().apply();
            Intent intent = new Intent(Menu.this, Login.class);
            startActivity(intent);
            finish();
        });

        refreshSpinners();

        // Optional: hide admin-only button if not admin
        if (!userRole.equals("admin")) {
            // example: hide an admin button
            // findViewById(R.id.admin_button).setVisibility(View.GONE);
        }
    }

    private void setupSpinner(Spinner spinner, String hint, String[] options) {
        List<String> items = new ArrayList<>();
        items.add(hint);
        items.addAll(Arrays.asList(options));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.DKGRAY);

                String topic = getItem(position);
                if (completedTopics.contains(topic)) {
                    textView.setTextColor(Color.GRAY);
                    textView.setEnabled(false);
                }
                return textView;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0, false);
    }

    private void setSpinnerListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedTopic = parent.getItemAtPosition(position).toString();
                    nextButton.setEnabled(true);
                    disableOtherSpinners(spinner);
                } else {
                    selectedTopic = "";
                    nextButton.setEnabled(false);
                    enableAllSpinners();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void disableOtherSpinners(Spinner selectedSpinner) {
        if (selectedSpinner != spinnerEasy) spinnerEasy.setEnabled(false);
        if (selectedSpinner != spinnerMedium) spinnerMedium.setEnabled(false);
        if (selectedSpinner != spinnerHard) spinnerHard.setEnabled(false);
    }

    private void enableAllSpinners() {
        spinnerEasy.setEnabled(true);
        spinnerMedium.setEnabled(true);
        spinnerHard.setEnabled(true);
    }

    private void handleNextButtonClick() {
        Intent intent;
        if (selectedTopic.equals("Data Types")) {
            intent = new Intent(Menu.this, DataTypes.class);
        } else if (selectedTopic.equals("Array") || selectedTopic.equals("Array and ArrayList")) {
            intent = new Intent(Menu.this, Array.class);
        } else {
            intent = new Intent(Menu.this, wij.class);
        }
        intent.putExtra("topic", selectedTopic);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String completedTopic = data.getStringExtra("completedTopic");
            if (completedTopic != null) {
                completedTopics.add(completedTopic);

                SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putStringSet("FinishedCourses", new HashSet<>(completedTopics));
                editor.apply();

                refreshSpinners();
            }
        }
    }

    private void refreshSpinners() {
        setupSpinner(spinnerEasy, "Easy", new String[]{"What is Java"});
        setupSpinner(spinnerMedium, "Medium", new String[]{"Data Types"});
        setupSpinner(spinnerHard, "Hard", new String[]{"Array and ArrayList"});
    }
}
