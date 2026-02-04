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

public class Quiz3 extends AppCompatActivity {
    private RadioGroup q11G, q12G, q13G, q14G, q15G;
    private Button submitBtn;
    private final String TOPIC = "Arrays";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);

        q11G = findViewById(R.id.q11_group);
        q12G = findViewById(R.id.q12_group);
        q13G = findViewById(R.id.q13_group);
        q14G = findViewById(R.id.q14_group);
        q15G = findViewById(R.id.q15_group);
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
        // Verify answers using the IDs from your activity_quiz3.xml
        s += verifyAnswer(q11G, R.id.q11_b);
        s += verifyAnswer(q12G, R.id.q12_a);
        s += verifyAnswer(q13G, R.id.q13_c);
        s += verifyAnswer(q14G, R.id.q14_a);
        s += verifyAnswer(q15G, R.id.q15_b);
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
        return q11G.getCheckedRadioButtonId() != -1 && q12G.getCheckedRadioButtonId() != -1 &&
                q13G.getCheckedRadioButtonId() != -1 && q14G.getCheckedRadioButtonId() != -1 &&
                q15G.getCheckedRadioButtonId() != -1;
    }

    // --- UPDATED: Method to show score AND correct answers for Quiz 3 ---
    private void showResult(int score) {
        StringBuilder message = new StringBuilder();
        message.append("Your Score: ").append(score).append("/5\n\n");
        message.append("Correct Answers:\n");

        // Dynamically fetching the text from the correct radio buttons
        message.append("11. ").append(((RadioButton) findViewById(R.id.q11_b)).getText()).append("\n");
        message.append("12. ").append(((RadioButton) findViewById(R.id.q12_a)).getText()).append("\n");
        message.append("13. ").append(((RadioButton) findViewById(R.id.q13_c)).getText()).append("\n");
        message.append("14. ").append(((RadioButton) findViewById(R.id.q14_a)).getText()).append("\n");
        message.append("15. ").append(((RadioButton) findViewById(R.id.q15_b)).getText()).append("\n");

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