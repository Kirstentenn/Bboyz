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

    // Handles the transition back from QuizActivity to the Menu
    private final ActivityResultLauncher<Intent> quizLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("completedTopic", topic);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wij);

        // UI Initialization
        javaInfoDisplay = findViewById(R.id.java_full_info);
        topic = getIntent().getStringExtra("topic");

        // Load content from res/raw/java_info.txt
        javaInfoDisplay.setText(readRawTextFile(R.raw.java_info));

        Button backButton = findViewById(R.id.button3);
        backButton.setOnClickListener(view -> finish());

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(view -> {
            // Transitions to the Quiz for this specific topic
            Intent intent = new Intent(wij.this, QuizActivity.class);
            intent.putExtra("topic", topic);
            quizLauncher.launch(intent);
        });
    }

    private String readRawTextFile(int resId) {
        StringBuilder stringBuilder = new StringBuilder();
        // try-with-resources ensures the stream is closed automatically
        try (InputStream inputStream = getResources().openRawResource(resId);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading raw file", e);
            return "Error loading content.";
        }
        return stringBuilder.toString();
    }
}