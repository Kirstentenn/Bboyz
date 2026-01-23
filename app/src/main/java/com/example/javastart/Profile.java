package com.example.javastart;

import android.content.Intent; // Added for navigation
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.widget.TextView;

import android.widget.Button; // Added for button functionality
import android.text.method.ScrollingMovementMethod;
import java.util.Set;

public class Profile extends AppCompatActivity {

    private TextView nameTextView, courseTextView, emailTextView, finishedCoursesTextView, finishedCoursesLabel; // Added finishedCoursesLabel
    private Button button4; // Added button4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Ensure this matches your profile layout file

        // Initialize TextViews
        nameTextView = findViewById(R.id.profile_name);
        courseTextView = findViewById(R.id.profile_course);
        emailTextView = findViewById(R.id.profile_email);
        finishedCoursesTextView = findViewById(R.id.fincor); // Added for finished courses
        finishedCoursesLabel = findViewById(R.id.finished_courses_label); // Added label

        // Enable text wrapping and scrolling for courseTextView
        courseTextView.setMovementMethod(new ScrollingMovementMethod());

        // Initialize button4
        button4 = findViewById(R.id.button4);

        // Set button4 to navigate back to Menu.java
        button4.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, Menu.class);
            startActivity(intent);
            finish(); // Close the current activity
        });

        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "No Name");
        String course = sharedPreferences.getString("course", "No Course");
        String email = sharedPreferences.getString("email", "No Email");

        // Set retrieved data to TextViews
        nameTextView.setText(name);
        courseTextView.setText(course);
        emailTextView.setText(email);

        // Load finished courses from SharedPreferences
        Set<String> finishedCourses = sharedPreferences.getStringSet("FinishedCourses", new java.util.HashSet<>());

        finishedCoursesLabel.setText("Finished Courses:"); // Always display the label

        if (finishedCourses != null && !finishedCourses.isEmpty()) {
            StringBuilder coursesList = new StringBuilder();
            for (String courseName : finishedCourses) {
                coursesList.append("• ").append(courseName).append("\n"); // Bullet points
            }
            finishedCoursesTextView.setText(coursesList.toString().trim());
        } else {
            finishedCoursesTextView.setText("No finished courses yet.");
        }
    }
}
