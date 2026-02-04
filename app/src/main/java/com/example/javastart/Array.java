package com.example.javastart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.util.Scanner;

public class Array extends AppCompatActivity {
    private String topic;
    private TextView contentTextView;

    private final ActivityResultLauncher<Intent> quizLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This ensures the current screen shows your lesson XML
        setContentView(R.layout.activity_array);

        contentTextView = findViewById(R.id.what_are_arraylist);

        topic = getIntent().getStringExtra("topic");
        if (topic == null) topic = "Arrays";

        loadLessonContent();

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(view -> {
            Intent intent = new Intent(Array.this, Quiz3.class);
            intent.putExtra("topic", topic);
            quizLauncher.launch(intent);
        });

        findViewById(R.id.button3).setOnClickListener(v -> finish());
    }

    private void loadLessonContent() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.array);
            Scanner scanner = new Scanner(inputStream);
            StringBuilder builder = new StringBuilder();

            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\n");
            }
            scanner.close();

            contentTextView.setText(builder.toString());
        } catch (Exception e) {
            contentTextView.setText("Error: array.txt not found in res/raw");
        }
    }
}