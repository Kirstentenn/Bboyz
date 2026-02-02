package com.example.javastart;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private RadioGroup q1Group, q2Group, q3Group, q4Group, q5Group;
    private Button submitButton;
    private String topic;
    private final String[] correctAnswers = {"B", "C", "A", "B", "C"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        topic = getIntent().getStringExtra("topic");
        if (topic == null) topic = "What is Java";

        q1Group = findViewById(R.id.q1_group);
        q2Group = findViewById(R.id.q2_group);
        q3Group = findViewById(R.id.q3_group);
        q4Group = findViewById(R.id.q4_group);
        q5Group = findViewById(R.id.q5_group);
        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(v -> {
            if (areAllQuestionsAnswered()) {
                int score = calculateScore();
                showScorePopup(score);
            } else {
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean areAllQuestionsAnswered() {
        return q1Group.getCheckedRadioButtonId() != -1 &&
                q2Group.getCheckedRadioButtonId() != -1 &&
                q3Group.getCheckedRadioButtonId() != -1 &&
                q4Group.getCheckedRadioButtonId() != -1 &&
                q5Group.getCheckedRadioButtonId() != -1;
    }

    private int calculateScore() {
        int score = 0;
        if (q1Group.getCheckedRadioButtonId() == R.id.q1_b) score++;
        if (q2Group.getCheckedRadioButtonId() == R.id.q2_c) score++;
        if (q3Group.getCheckedRadioButtonId() == R.id.q3_a) score++;
        if (q4Group.getCheckedRadioButtonId() == R.id.q4_b) score++;
        if (q5Group.getCheckedRadioButtonId() == R.id.q5_c) score++;
        return score;
    }

    private void showScorePopup(int score) {
        new AlertDialog.Builder(this)
                .setTitle("Quiz Result")
                .setMessage("Your Score: " + score + "/5")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    saveScoreToDatabase(score);
                    markTopicAsComplete();
                })
                .show();
    }

    private void saveScoreToDatabase(int score) {
        String url = "http://10.0.2.2/javastart_api/save_score.php";
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sp.getString("email", "unknown");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {},
                error -> Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("topic", topic);
                params.put("score", String.valueOf(score));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void markTopicAsComplete() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("completedTopic", topic);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}