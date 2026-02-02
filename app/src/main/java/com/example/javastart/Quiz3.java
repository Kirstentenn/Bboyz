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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Quiz3 extends AppCompatActivity {

    private RadioGroup q11Group, q12Group, q13Group, q14Group, q15Group;
    private Button submitButton;
    private String topic;
    private final String[] correctAnswers = {"A", "B", "C", "B", "C"}; // Fixed space typo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);

        topic = getIntent().getStringExtra("topic");
        if (topic == null) topic = "Quiz 3";

        q11Group = findViewById(R.id.q11_group);
        q12Group = findViewById(R.id.q12_group);
        q13Group = findViewById(R.id.q13_group);
        q14Group = findViewById(R.id.q14_group);
        q15Group = findViewById(R.id.q15_group);
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
        return q11Group.getCheckedRadioButtonId() != -1 &&
                q12Group.getCheckedRadioButtonId() != -1 &&
                q13Group.getCheckedRadioButtonId() != -1 &&
                q14Group.getCheckedRadioButtonId() != -1 &&
                q15Group.getCheckedRadioButtonId() != -1;
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
        if (q11Group.getCheckedRadioButtonId() == R.id.q11_a) score++;
        if (q12Group.getCheckedRadioButtonId() == R.id.q12_b) score++;
        if (q13Group.getCheckedRadioButtonId() == R.id.q13_c) score++;
        if (q14Group.getCheckedRadioButtonId() == R.id.q14_b) score++;
        if (q15Group.getCheckedRadioButtonId() == R.id.q15_c) score++;
        return score;
    }

    private void showScorePopup(int score) {
        StringBuilder answersText = new StringBuilder();
        for (int i = 0; i < correctAnswers.length; i++) {
            answersText.append("Q").append(i + 11).append(": ").append(correctAnswers[i]).append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Quiz Result")
                .setMessage("Your Score: " + score + "/5\n\nCorrect Answers:\n" + answersText)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    saveScoreToDatabase(score); // Save to XAMPP
                    markTopicAsComplete();
                })
                .show();
    }

    private void saveScoreToDatabase(int score) {
        String url = "http://10.0.2.2/javastart_api/save_score.php";

        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sp.getString("email", "unknown");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> { /* Database update successful */ },
                error -> Toast.makeText(Quiz3.this, "Network Error: Score not saved", Toast.LENGTH_SHORT).show()
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