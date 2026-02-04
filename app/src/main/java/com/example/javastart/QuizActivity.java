package com.example.javastart;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {
    private RadioGroup q1G, q2G, q3G, q4G, q5G;
    private Button submitBtn;
    private final String TOPIC = "What is Java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        q1G = findViewById(R.id.q1_group);
        q2G = findViewById(R.id.q2_group);
        q3G = findViewById(R.id.q3_group);
        q4G = findViewById(R.id.q4_group);
        q5G = findViewById(R.id.q5_group);
        submitBtn = findViewById(R.id.submit_button);

        submitBtn.setOnClickListener(v -> {
            if (isInputValid()) {
                submitBtn.setEnabled(false);
                int score = calculateAndHighlight();
                new Handler(Looper.getMainLooper()).postDelayed(() -> showResult(score), 400);
            } else {
                Toast.makeText(this, "Answer all questions!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int calculateAndHighlight() {
        int s = 0;
        s += verifyAnswer(q1G, R.id.q1_b);
        s += verifyAnswer(q2G, R.id.q2_b);
        s += verifyAnswer(q3G, R.id.q3_a);
        s += verifyAnswer(q4G, R.id.q4_b);
        s += verifyAnswer(q5G, R.id.q5_a);
        return s;
    }

    private int verifyAnswer(RadioGroup group, int correctId) {
        int selected = group.getCheckedRadioButtonId();
        RadioButton correctRb = findViewById(correctId);
        if (correctRb != null) correctRb.setTextColor(Color.parseColor("#388E3C"));
        if (selected == correctId) return 1;
        RadioButton wrongRb = findViewById(selected);
        if (wrongRb != null) wrongRb.setTextColor(Color.RED);
        return 0;
    }

    private boolean isInputValid() {
        return q1G.getCheckedRadioButtonId() != -1 && q2G.getCheckedRadioButtonId() != -1 &&
                q3G.getCheckedRadioButtonId() != -1 && q4G.getCheckedRadioButtonId() != -1 &&
                q5G.getCheckedRadioButtonId() != -1;
    }

    // --- UPDATED: Method to show score AND correct answers ---
    private void showResult(int score) {
        StringBuilder message = new StringBuilder();
        message.append("Final Score: ").append(score).append("/5\n\n");
        message.append("Correct Answers:\n");
        message.append("1. ").append(((RadioButton) findViewById(R.id.q1_b)).getText()).append("\n");
        message.append("2. ").append(((RadioButton) findViewById(R.id.q2_b)).getText()).append("\n");
        message.append("3. ").append(((RadioButton) findViewById(R.id.q3_a)).getText()).append("\n");
        message.append("4. ").append(((RadioButton) findViewById(R.id.q4_b)).getText()).append("\n");
        message.append("5. ").append(((RadioButton) findViewById(R.id.q5_a)).getText()).append("\n");

        new AlertDialog.Builder(this)
                .setTitle("Quiz Result")
                .setMessage(message.toString())
                .setCancelable(false)
                .setPositiveButton("OK", (d, w) -> syncScore(score))
                .show();
    }

    private void syncScore(int score) {
        String url = "http://10.0.2.2/javastart_api/save_score.php";
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = sp.getString("email", "unknown");

        StringRequest req = new StringRequest(Request.Method.POST, url,
                res -> {
                    setResult(RESULT_OK); // Tells Menu to refresh UI
                    finish();
                },
                err -> {
                    setResult(RESULT_OK);
                    finish();
                }) {
            @Override protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("email", email);
                p.put("topic", TOPIC);
                p.put("score", String.valueOf(score));
                return p;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }
}