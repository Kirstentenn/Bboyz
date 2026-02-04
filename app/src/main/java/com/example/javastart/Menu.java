package com.example.javastart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = sp.getString("email", "");

        findViewById(R.id.profile_icon).setOnClickListener(v -> startActivity(new Intent(this, Profile.class)));

        logoutButton.setOnClickListener(v -> {
            sp.edit().clear().apply();
            Intent intent = new Intent(Menu.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        nextButton.setOnClickListener(v -> handleNext());
        fetchProgress();
    }

    private void fetchProgress() {
        String url = "http://10.0.2.2/javastart_api/get_scores.php?email=" + userEmail;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                res -> {
                    completedTopics.clear();
                    Iterator<String> keys = res.keys();
                    while(keys.hasNext()) {
                        completedTopics.add(keys.next());
                    }
                    setupSpinners();
                }, e -> setupSpinners());
        Volley.newRequestQueue(this).add(req);
    }

    private void setupSpinners() {
        // These strings must match exactly what is saved in the 'topic' column of your DB
        setupSpinner(spinnerEasy, "Select Easy Topic", new String[]{"What is Java"});
        setupSpinner(spinnerMedium, "Select Medium Topic", new String[]{"Data Types"});
        setupSpinner(spinnerHard, "Select Hard Topic", new String[]{"Arrays"});

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                if (pos > 0) {
                    selectedTopic = parent.getItemAtPosition(pos).toString();

                    // Logic to ensure only one spinner has a selection at a time
                    if (parent == spinnerEasy) {
                        spinnerMedium.setSelection(0, false);
                        spinnerHard.setSelection(0, false);
                    } else if (parent == spinnerMedium) {
                        spinnerEasy.setSelection(0, false);
                        spinnerHard.setSelection(0, false);
                    } else if (parent == spinnerHard) {
                        spinnerEasy.setSelection(0, false);
                        spinnerMedium.setSelection(0, false);
                    }

                    // Check if topic is already done
                    if (completedTopics.contains(selectedTopic)) {
                        Toast.makeText(Menu.this, "Done! Go to Profile to retake.", Toast.LENGTH_SHORT).show();
                        nextButton.setEnabled(false);
                        nextButton.setAlpha(0.4f);
                    } else {
                        nextButton.setEnabled(true);
                        nextButton.setAlpha(1.0f);
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        };

        spinnerEasy.setOnItemSelectedListener(listener);
        spinnerMedium.setOnItemSelectedListener(listener);
        spinnerHard.setOnItemSelectedListener(listener);
    }

    private void setupSpinner(Spinner s, String hint, String[] opts) {
        List<String> items = new ArrayList<>();
        items.add(hint);
        items.addAll(Arrays.asList(opts));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
            @Override
            public View getDropDownView(int pos, View cv, ViewGroup p) {
                TextView tv = (TextView) super.getDropDownView(pos, cv, p);
                String itemText = getItem(pos);
                if (completedTopics.contains(itemText)) {
                    tv.setTextColor(Color.GRAY);
                    tv.setText(itemText + " (Done)");
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return tv;
            }
        };
        s.setAdapter(adapter);
    }

    private void handleNext() {
        if (selectedTopic.isEmpty()) return;

        Class<?> target;
        switch (selectedTopic) {
            case "Arrays":
                target = Array.class;
                break;
            case "Data Types":
                target = DataTypes.class;
                break;
            default:
                target = wij.class; // For "What is Java"
                break;
        }

        Intent intent = new Intent(this, target);
        intent.putExtra("topic", selectedTopic);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh progress every time user comes back from a Quiz
        fetchProgress();
    }
}