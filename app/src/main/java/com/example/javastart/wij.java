package com.example.javastart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class wij extends AppCompatActivity {
    private static final String TAG = "wij";
    private String topic;
    private TextView javaInfoDisplay;

    // This catches the 'finish()' from QuizActivity and closes this screen too
    private final ActivityResultLauncher<Intent> quizLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                // When Quiz finishes, we finish this Info screen to return to Menu
                finish();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wij);

        javaInfoDisplay = findViewById(R.id.java_full_info);
        topic = getIntent().getStringExtra("topic");

        javaInfoDisplay.setText(readRawTextFile(R.raw.java_info));

        findViewById(R.id.button3).setOnClickListener(view -> finish());

        findViewById(R.id.next_button).setOnClickListener(view -> {
            Intent intent = new Intent(wij.this, QuizActivity.class);
            intent.putExtra("topic", topic);
            quizLauncher.launch(intent);
        });
    }

    private String readRawTextFile(int resId) {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getResources().openRawResource(resId);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
        } catch (IOException e) {
            Log.e(TAG, "Error reading raw file", e);
            return "Error loading content.";
        }
        return sb.toString();
    }
}