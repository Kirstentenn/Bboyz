package com.example.javastart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Set;

public class Profile extends AppCompatActivity {

    private TextView nameText, courseText, emailText, finishedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.profile_name);
        courseText = findViewById(R.id.profile_course);
        emailText = findViewById(R.id.profile_email);
        finishedText = findViewById(R.id.fincor);
        Button backBtn = findViewById(R.id.button4);

        finishedText.setMovementMethod(new ScrollingMovementMethod());

        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        nameText.setText(sp.getString("name", "User"));
        courseText.setText(sp.getString("course", "N/A"));
        emailText.setText(sp.getString("email", "N/A"));

        // Load progress
        Set<String> finished = sp.getStringSet("FinishedCourses", new HashSet<>());
        if (finished != null && !finished.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String s : finished) sb.append("• ").append(s).append("\n");
            finishedText.setText(sb.toString().trim());
        } else {
            finishedText.setText("No courses completed yet.");
        }

        backBtn.setOnClickListener(v -> finish());
    }
}