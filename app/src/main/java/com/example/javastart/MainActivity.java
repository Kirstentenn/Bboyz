// Package declaration: Defines the package where this class belongs.
package com.example.javastart;

import android.content.Intent; // Used for switching between activities.
import android.os.Bundle; // Needed for saving activity state.
import android.view.View; // Allows handling user interactions.
import android.widget.Button; // Represents a button in the UI.
import androidx.appcompat.app.AppCompatActivity; // Base class for activities.

// This is the main entry point of the app.
public class MainActivity extends AppCompatActivity {

    // Declare a button for starting the next activity.
    private Button start_Button;

    // onCreate() is called when the activity starts.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Sets the layout for this activity.

        // Link the button to its corresponding XML view.
        start_Button = findViewById(R.id.start_button);

        // Set an action when the start button is clicked.
        start_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate from MainActivity to Login activity.
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent); // Start the Login activity.
            }
        });
    }
}
