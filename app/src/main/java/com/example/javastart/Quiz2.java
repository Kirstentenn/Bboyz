package com.example.javastart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class Quiz2 extends AppCompatActivity {

    private RadioGroup q6Group, q7Group, q8Group, q9Group, q10Group;
    private Button submitButton;
    private String topic; // Stores the topic name
    private final String[] correctAnswers = {"B", "B", "B", "B", "A"}; // Correct answers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

        // Get the topic from intent
        topic = getIntent().getStringExtra("topic");

        // Initialize UI components
        q6Group = findViewById(R.id.q6_group);
        q7Group = findViewById(R.id.q7_group);
        q8Group = findViewById(R.id.q8_group);
        q9Group = findViewById(R.id.q9_group);
        q10Group = findViewById(R.id.q10_group);
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
        return q6Group.getCheckedRadioButtonId() != -1 &&
                q7Group.getCheckedRadioButtonId() != -1 &&
                q8Group.getCheckedRadioButtonId() != -1 &&
                q9Group.getCheckedRadioButtonId() != -1 &&
                q10Group.getCheckedRadioButtonId() != -1;
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
        int correctQ6 = R.id.q6_b;
        int correctQ7 = R.id.q7_b;
        int correctQ8 = R.id.q8_b;
        int correctQ9 = R.id.q9_b;
        int correctQ10 = R.id.q10_a;

        if (q6Group.getCheckedRadioButtonId() == correctQ6) score++;
        if (q7Group.getCheckedRadioButtonId() == correctQ7) score++;
        if (q8Group.getCheckedRadioButtonId() == correctQ8) score++;
        if (q9Group.getCheckedRadioButtonId() == correctQ9) score++;
        if (q10Group.getCheckedRadioButtonId() == correctQ10) score++;

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
