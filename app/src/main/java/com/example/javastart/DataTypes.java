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
import java.nio.charset.StandardCharsets;

public class DataTypes extends AppCompatActivity {
    private String topic;
    private TextView datatypesDisplay;

    private final ActivityResultLauncher<Intent> quizLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_types);

        topic = getIntent().getStringExtra("topic");
        datatypesDisplay = findViewById(R.id.whataredatatypes);
        datatypesDisplay.setText(readRawTextFile(R.raw.datatypes));

        findViewById(R.id.button3).setOnClickListener(view -> finish());

        findViewById(R.id.next_button).setOnClickListener(view -> {
            Intent intent = new Intent(DataTypes.this, Quiz2.class);
            intent.putExtra("topic", topic);
            quizLauncher.launch(intent);
        });
    }

    private String readRawTextFile(int resId) {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getResources().openRawResource(resId);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
        } catch (IOException e) { Log.e("DataTypes", "Error reading file", e); }
        return sb.toString();
    }
}