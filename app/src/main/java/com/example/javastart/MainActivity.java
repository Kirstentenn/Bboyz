package com.example.javastart;

import android.content.Intent; // Used for switching between activities.
import android.os.Bundle; // Needed for saving activity state.
import android.view.View; // Allows handling user interactions.
import android.widget.Button; // Represents a button in the UI.
import androidx.appcompat.app.AppCompatActivity; // Base class for activities.

public class MainActivity extends AppCompatActivity {

    private Button start_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Sets the layout for this activity.

        start_Button = findViewById(R.id.start_button);

        start_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent); // Start the Login activity.
            }
        });
    }
}
