package com.example.javastart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class Splash extends Activity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 5;

                handler.post(() -> progressBar.setProgress(progressStatus));

                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
            finish();
        }).start();
    }
}