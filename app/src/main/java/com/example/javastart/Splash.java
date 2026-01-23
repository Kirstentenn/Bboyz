package com.example.javastart;

import android.os.Bundle; // Required to save and restore activity state.
import android.app.Activity; // Base class for activities.
import android.content.Intent; // Used to switch between activities.
import android.os.Handler; // Used for handling delayed tasks.
import android.widget.ProgressBar; // Represents a progress bar in the UI.

// This class represents the splash screen that appears when the app starts.
public class Splash extends Activity {

    // Declare a ProgressBar variable.
    private ProgressBar progressBar;

    // Variable to track the progress of the progress bar.
    private int progressStatus = 0;

    // Handler to update the UI from a background thread.
    private Handler handler = new Handler();

    // onCreate() is called when the activity starts.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Sets the layout for this activity.

        // Link the progress bar to its XML view.
        progressBar = findViewById(R.id.progressBar);

        // Create a new thread to simulate a loading screen.
        new Thread(() -> {
            // Keep increasing progress until it reaches 100.
            while (progressStatus < 100) {
                progressStatus += 5; // Increase progress in steps of 5.

                // Update the progress bar on the UI thread.
                handler.post(() -> progressBar.setProgress(progressStatus));

                try {
                    Thread.sleep(150); // Pause for 150 milliseconds to control speed.
                } catch (InterruptedException e) {
                    e.printStackTrace(); // Print error message if an interruption occurs.
                }
            }

            // After loading is complete, move to MainActivity.
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the Splash activity so the user can't go back to it.
        }).start(); // Start the background thread.
    }
}
