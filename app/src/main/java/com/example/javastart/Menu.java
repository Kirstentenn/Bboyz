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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
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
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        spinnerEasy = findViewById(R.id.spinner_easy);
        spinnerMedium = findViewById(R.id.spinner_medium);
        spinnerHard = findViewById(R.id.spinner_hard);
        nextButton = findViewById(R.id.button2);
        logoutButton = findViewById(R.id.button5);
        ImageView profileIcon = findViewById(R.id.profile_icon);

        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = sp.getString("email", "");

        // Load local progress
        completedTopics = new HashSet<>(sp.getStringSet("FinishedCourses", new HashSet<>()));

        fetchProgressFromServer();
        setupSpinners();

        nextButton.setEnabled(false);
        nextButton.setOnClickListener(v -> handleNextButtonClick());

        profileIcon.setOnClickListener(v -> startActivity(new Intent(Menu.this, Profile.class)));

        logoutButton.setOnClickListener(v -> {
            sp.edit().clear().apply();
            startActivity(new Intent(Menu.this, Login.class));
            finish();
        });
    }

    private void fetchProgressFromServer() {
        String url = "http://10.0.2.2/javastart_api/get_progress.php?email=" + userEmail;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            completedTopics.add(response.getString(i));
                        }
                        saveLocalProgress();
                        refreshSpinners();
                    } catch (JSONException e) { e.printStackTrace(); }
                },
                error -> Toast.makeText(this, "Sync failed", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void saveLocalProgress() {
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        sp.edit().putStringSet("FinishedCourses", new HashSet<>(completedTopics)).apply();
    }

    private void setupSpinners() {
        setupSpinner(spinnerEasy, "Easy", new String[]{"What is Java"});
        setupSpinner(spinnerMedium, "Medium", new String[]{"Data Types"});
        setupSpinner(spinnerHard, "Hard", new String[]{"Array and ArrayList"});

        setSpinnerListener(spinnerEasy);
        setSpinnerListener(spinnerMedium);
        setSpinnerListener(spinnerHard);
    }

    private void setupSpinner(Spinner spinner, String hint, String[] options) {
        List<String> items = new ArrayList<>();
        items.add(hint);
        items.addAll(Arrays.asList(options));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items) {
            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                String topic = getItem(position);
                if (completedTopics.contains(topic)) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.WHITE);
                }
                return textView;
            }
        };
        spinner.setAdapter(adapter);
    }

    private void setSpinnerListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String topic = parent.getItemAtPosition(position).toString();
                    if (completedTopics.contains(topic)) {
                        Toast.makeText(Menu.this, "Topic finished!", Toast.LENGTH_SHORT).show();
                        nextButton.setEnabled(false);
                    } else {
                        selectedTopic = topic;
                        nextButton.setEnabled(true);
                        disableOtherSpinners(spinner);
                    }
                } else {
                    if (spinnerEasy.getSelectedItemPosition() == 0 &&
                            spinnerMedium.getSelectedItemPosition() == 0 &&
                            spinnerHard.getSelectedItemPosition() == 0) {
                        nextButton.setEnabled(false);
                        enableAllSpinners();
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void handleNextButtonClick() {
        Intent intent;
        if (selectedTopic.equals("Data Types")) {
            intent = new Intent(Menu.this, DataTypes.class);
        } else if (selectedTopic.equals("Array and ArrayList")) {
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
            String finished = data.getStringExtra("completedTopic");
            if (finished != null) {
                completedTopics.add(finished);
                saveLocalProgress();
                refreshSpinners();
                fetchProgressFromServer(); // Sync with server
            }
        }
    }

    private void refreshSpinners() { setupSpinners(); }

    private void disableOtherSpinners(Spinner selected) {
        if (selected != spinnerEasy) spinnerEasy.setEnabled(false);
        if (selected != spinnerMedium) spinnerMedium.setEnabled(false);
        if (selected != spinnerHard) spinnerHard.setEnabled(false);
    }

    private void enableAllSpinners() {
        spinnerEasy.setEnabled(true);
        spinnerMedium.setEnabled(true);
        spinnerHard.setEnabled(true);
    }
}