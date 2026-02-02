package com.example.javastart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Quiz2 extends AppCompatActivity {

    private RadioGroup q6Group, q7Group, q8Group, q9Group, q10Group;
    private Button submitButton;
    private String topic;
    private final String[] correctAnswers = {"B", "B", "B", "B", "A"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

        topic = getIntent().getStringExtra("topic");
        if (topic == null) topic = "Quiz 2"; // Fallback name

        q6Group = findViewById(R.id.q6_group);
        q7Group = findViewById(R.id.q7_group);
        q8Group = findViewById(R.id.q8_group);
        q9Group = findViewById(R.id.q9_group);
        q10Group = findViewById(R.id.q10_group);
        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(v -> {
            if (areAllQuestionsAnswered()) {
                int score = calculateScore();
                showScorePopup(score);
            } else {
                showIncompleteQuizAlert();
            }
        });
    }

    private boolean areAllQuestionsAnswered() {
        return q6Group.getCheckedRadioButtonId() != -1 &&
                q7Group.getCheckedRadioButtonId() != -1 &&
                q8Group.getCheckedRadioButtonId() != -1 &&
                q9Group.getCheckedRadioButtonId() != -1 &&
                q10Group.getCheckedRadioButtonId() != -1;
    }

    private void showIncompleteQuizAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Incomplete Quiz")
                .setMessage("Please answer all questions before submitting.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private int calculateScore() {
        int score = 0;
        if (q6Group.getCheckedRadioButtonId() == R.id.q6_b) score++;
        if (q7Group.getCheckedRadioButtonId() == R.id.q7_b) score++;
        if (q8Group.getCheckedRadioButtonId() == R.id.q8_b) score++;
        if (q9Group.getCheckedRadioButtonId() == R.id.q9_b) score++;
        if (q10Group.getCheckedRadioButtonId() == R.id.q10_a) score++;
        return score;
    }

    private void showScorePopup(int score) {
        StringBuilder answersText = new StringBuilder();
        for (int i = 0; i < correctAnswers.length; i++) {
            answersText.append("Q").append(i + 6).append(": ").append(correctAnswers[i]).append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Quiz Result")
                .setMessage("Your Score: " + score + "/5\n\nCorrect Answers:\n" + answersText)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    saveScoreToDatabase(score); // DATABASE SAVE
                    markTopicAsComplete();
                })
                .show();
    }

    private void saveScoreToDatabase(int score) {
        String url = "http://10.0.2.2/javastart_api/save_score.php";

        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sp.getString("email", "unknown");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Score saved on server
                },
                error -> Toast.makeText(Quiz2.this, "Could not sync score to server", Toast.LENGTH_SHORT).show()
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