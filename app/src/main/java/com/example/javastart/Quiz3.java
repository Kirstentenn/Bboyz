package com.example.javastart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class Quiz3 extends AppCompatActivity {

    private RadioGroup q11Group, q12Group, q13Group, q14Group, q15Group;
    private Button submitButton;
    private String topic; // Stores the topic name
    private final String[] correctAnswers = {"A", "B", "C", "B", "C "}; // Correct answers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);

        // Get the topic from intent
        topic = getIntent().getStringExtra("topic");

        // Initialize UI components
        q11Group = findViewById(R.id.q11_group);
        q12Group = findViewById(R.id.q12_group);
        q13Group = findViewById(R.id.q13_group);
        q14Group = findViewById(R.id.q14_group);
        q15Group = findViewById(R.id.q15_group);
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
        return q11Group.getCheckedRadioButtonId() != -1 &&
                q12Group.getCheckedRadioButtonId() != -1 &&
                q13Group.getCheckedRadioButtonId() != -1 &&
                q14Group.getCheckedRadioButtonId() != -1 &&
                q15Group.getCheckedRadioButtonId() != -1;
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
        int correctQ6 = R.id.q11_a;
        int correctQ7 = R.id.q12_b;
        int correctQ8 = R.id.q13_c;
        int correctQ9 = R.id.q14_b;
        int correctQ10 = R.id.q15_c;

        if (q11Group.getCheckedRadioButtonId() == correctQ6) score++;
        if (q12Group.getCheckedRadioButtonId() == correctQ7) score++;
        if (q13Group.getCheckedRadioButtonId() == correctQ8) score++;
        if (q14Group.getCheckedRadioButtonId() == correctQ9) score++;
        if (q15Group.getCheckedRadioButtonId() == correctQ10) score++;

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
