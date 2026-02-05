package com.example.javastart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    private TextView finishedText;
    private Button btnJava, btnDataTypes, btnArrays;
    private String userEmail;
    private Map<String, String> scores = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // UI Initializations
        finishedText = findViewById(R.id.fincor);
        btnJava = findViewById(R.id.btn_retake_java);
        btnDataTypes = findViewById(R.id.btn_retake_datatypes);
        btnArrays = findViewById(R.id.btn_retake_arrays);

        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = sp.getString("email", "");

        // Note: Using "fullname" to match your DB column name logic
        String name = sp.getString("fullname", sp.getString("name", "User"));
        ((TextView)findViewById(R.id.profile_name)).setText("Name: " + name);
        ((TextView)findViewById(R.id.profile_email)).setText("Email: " + userEmail);

        fetchScores();

        // Button Listeners - Exact matching of Topic Strings is critical here
        btnJava.setOnClickListener(v -> confirmRetake("What is Java", QuizActivity.class));
        btnDataTypes.setOnClickListener(v -> confirmRetake("Data Types", Quiz2.class));
        btnArrays.setOnClickListener(v -> confirmRetake("Arrays", Quiz3.class));

        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
    }

    private void fetchScores() {
        String url = "http://10.0.2.2/javastart_api/get_scores.php?email=" + userEmail;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                res -> {
                    scores.clear();
                    String[] topics = {"What is Java", "Data Types", "Arrays"};
                    for (String t : topics) {
                        // Use res.has() to check for keys, including the one with the space
                        if (res.has(t)) {
                            scores.put(t, res.optString(t, "0"));
                        }
                    }
                    updateUI();
                }, e -> {
            Log.e("PROFILE_ERROR", "Volley Error: " + e.toString());
            // Fallback: Clear scores and show 0s instead of a hard error message
            scores.clear();
            updateUI();
            Toast.makeText(this, "Syncing issue. Please check connection.", Toast.LENGTH_SHORT).show();
        });
        Volley.newRequestQueue(this).add(req);
    }

    private void updateUI() {
        StringBuilder sb = new StringBuilder("QUIZ PROGRESS:\n\n");
        // Using getOrDefault to safely handle cases where the topic isn't in the DB yet
        sb.append("• Java: ").append(scores.getOrDefault("What is Java", "0")).append("/5\n");
        sb.append("• Data Types: ").append(scores.getOrDefault("Data Types", "0")).append("/5\n");
        sb.append("• Arrays: ").append(scores.getOrDefault("Arrays", "0")).append("/5");
        finishedText.setText(sb.toString());

        // Update button appearance (active if score exists)
        setupBtn(btnJava, scores.containsKey("What is Java"));
        setupBtn(btnDataTypes, scores.containsKey("Data Types"));
        setupBtn(btnArrays, scores.containsKey("Arrays"));
    }

    private void setupBtn(Button b, boolean active) {
        if (b != null) {
            b.setEnabled(active);
            b.setAlpha(active ? 1.0f : 0.4f);
        }
    }

    private void confirmRetake(String topic, Class<?> quizClass) {
        new AlertDialog.Builder(this)
                .setTitle("Retake Quiz?")
                .setMessage("Reset your current score for '" + topic + "' and start the quiz again?")
                .setPositiveButton("Yes", (d, w) -> resetScoreAndStart(topic, quizClass))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void resetScoreAndStart(String topic, Class<?> quizClass) {
        String url = "http://10.0.2.2/javastart_api/reset_specific_quiz.php";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                res -> {
                    // Success: Go to the quiz and finish this profile activity
                    startActivity(new Intent(this, quizClass));
                    finish();
                },
                err -> {
                    Log.e("RESET_ERROR", "Error resetting: " + err.toString());
                    Toast.makeText(this, "Failed to reset score. Check server.", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("email", userEmail);
                p.put("topic", topic);
                return p;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh scores automatically if the user comes back to this page
        fetchScores();
    }
}