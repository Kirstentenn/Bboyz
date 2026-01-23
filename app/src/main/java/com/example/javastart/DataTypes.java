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

public class DataTypes extends AppCompatActivity {

    private static final String TAG = "DataTypes"; // For logging
    private String topic;
    private TextView datatypes; // Added missing TextView declaration

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
        setContentView(R.layout.activity_data_types);

        // Initialize the missing TextView
        datatypes = findViewById(R.id.whataredatatypes);

        // Load text from raw resource
        TextView datatypes = findViewById(R.id.whataredatatypes);
        datatypes.setText(readRawTextFile(R.raw.datatypes));

        topic = getIntent().getStringExtra("topic");

        Button backButton = findViewById(R.id.button3);
        backButton.setOnClickListener(view -> finish());

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(view -> {
            Intent intent = new Intent(DataTypes.this, Quiz2.class);
            intent.putExtra("topic", topic);
            quizLauncher.launch(intent); // Use new method instead of startActivityForResult
        });
    }

    private String readRawTextFile(int resId) {
        InputStream inputStream = getResources().openRawResource(resId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Error reading file", e); // Use logging instead of printStackTrace()
        }
        return stringBuilder.toString();
    }
}
