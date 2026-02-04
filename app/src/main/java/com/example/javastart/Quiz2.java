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

public class Quiz2 extends AppCompatActivity {
    private RadioGroup q6G, q7G, q8G, q9G, q10G;
    private Button submitBtn;
    private final String TOPIC = "Data Types";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

        q6G = findViewById(R.id.q6_group);
        q7G = findViewById(R.id.q7_group);
        q8G = findViewById(R.id.q8_group);
        q9G = findViewById(R.id.q9_group);
        q10G = findViewById(R.id.q10_group);
        submitBtn = findViewById(R.id.submit_button);

        submitBtn.setOnClickListener(v -> {
            if (isInputValid()) {
                submitBtn.setEnabled(false);
                int score = calculateAndHighlight();
                new Handler(Looper.getMainLooper()).postDelayed(() -> showResult(score), 600);
            } else {
                Toast.makeText(this, "Please answer all questions!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int calculateAndHighlight() {
        int s = 0;
        s += verifyAnswer(q6G, R.id.q6_b);
        s += verifyAnswer(q7G, R.id.q7_b);
        s += verifyAnswer(q8G, R.id.q8_b);
        s += verifyAnswer(q9G, R.id.q9_b);
        s += verifyAnswer(q10G, R.id.q10_a);
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
        return q6G.getCheckedRadioButtonId() != -1 && q7G.getCheckedRadioButtonId() != -1 &&
                q8G.getCheckedRadioButtonId() != -1 && q9G.getCheckedRadioButtonId() != -1 &&
                q10G.getCheckedRadioButtonId() != -1;
    }

    // --- UPDATED: Method to show score AND correct answers for Quiz 2 ---
    private void showResult(int score) {
        StringBuilder message = new StringBuilder();
        message.append("Your Score: ").append(score).append("/5\n\n");
        message.append("Correct Answers:\n");

        // Pulling text directly from the RadioButtons to avoid typos
        message.append("6. ").append(((RadioButton) findViewById(R.id.q6_b)).getText()).append("\n");
        message.append("7. ").append(((RadioButton) findViewById(R.id.q7_b)).getText()).append("\n");
        message.append("8. ").append(((RadioButton) findViewById(R.id.q8_b)).getText()).append("\n");
        message.append("9. ").append(((RadioButton) findViewById(R.id.q9_b)).getText()).append("\n");
        message.append("10. ").append(((RadioButton) findViewById(R.id.q10_a)).getText()).append("\n");

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
                    setResult(RESULT_OK);
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