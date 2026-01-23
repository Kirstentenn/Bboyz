package com.example.javastart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class wij extends AppCompatActivity {

    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wij);

        TextView javaInfo = findViewById(R.id.java_full_info);
        javaInfo.setText(readRawTextFile(R.raw.java_info));

        topic = getIntent().getStringExtra("topic");

        Button backButton = findViewById(R.id.button3);
        backButton.setOnClickListener(view -> finish());

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(view -> {
            Intent intent = new Intent(wij.this, QuizActivity.class);
            intent.putExtra("topic", topic);
            startActivityForResult(intent, 1);
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
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("completedTopic", topic);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
