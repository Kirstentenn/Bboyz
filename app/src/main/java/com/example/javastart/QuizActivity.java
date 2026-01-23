package com.example.javastart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class QuizActivity extends AppCompatActivity {

    private RadioGroup q1Group, q2Group, q3Group, q4Group, q5Group;
    private Button submitButton;
    private String topic; // Stores the topic name
    private final String[] correctAnswers = {"B", "C", "A", "B", "C"}; // Correct answers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get the topic from intent
        topic = getIntent().getStringExtra("topic");

        // Initialize UI components
        q1Group = findViewById(R.id.q1_group);
        q2Group = findViewById(R.id.q2_group);
        q3Group = findViewById(R.id.q3_group);
        q4Group = findViewById(R.id.q4_group);
        q5Group = findViewById(R.id.q5_group);
        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areAllQuestionsAnswered()) {
                    int score = calculateScore();
                    showScorePopup(score);
                } else {
                    showIncompleteQuizAlert();
                }
            }
        });
    }

    private boolean areAllQuestionsAnswered() {
        return q1Group.getCheckedRadioButtonId() != -1 &&
                q2Group.getCheckedRadioButtonId() != -1 &&
                q3Group.getCheckedRadioButtonId() != -1 &&
                q4Group.getCheckedRadioButtonId() != -1 &&
                q5Group.getCheckedRadioButtonId() != -1;
    }

    private void showIncompleteQuizAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Incomplete Quiz")
                .setMessage("Please answer all questions before submitting the quiz.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int calculateScore() {
        int score = 0;

        // Correct answer IDs
        int correctQ1 = R.id.q1_b;
        int correctQ2 = R.id.q2_c;
        int correctQ3 = R.id.q3_a;
        int correctQ4 = R.id.q4_b;
        int correctQ5 = R.id.q5_c;

        if (q1Group.getCheckedRadioButtonId() == correctQ1) score++;
        if (q2Group.getCheckedRadioButtonId() == correctQ2) score++;
        if (q3Group.getCheckedRadioButtonId() == correctQ3) score++;
        if (q4Group.getCheckedRadioButtonId() == correctQ4) score++;
        if (q5Group.getCheckedRadioButtonId() == correctQ5) score++;

        return score;
    }

    private void showScorePopup(int score) {
        // Format correct answers
        StringBuilder answersText = new StringBuilder();
        for (int i = 0; i < correctAnswers.length; i++) {
            answersText.append("Q").append(i + 1).append(": ").append(correctAnswers[i]).append("\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Result")
                .setMessage("Your Score: " + score + "/5\n\nCorrect Answers:\n" + answersText)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        markTopicAsComplete();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void markTopicAsComplete() {
        // Send the completed topic back to Menu.java
        Intent resultIntent = new Intent();
        resultIntent.putExtra("completedTopic", topic);
        setResult(RESULT_OK, resultIntent);
        finish(); // Close the quiz activity
    }
}
