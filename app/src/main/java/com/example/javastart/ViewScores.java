package com.example.javastart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewScores extends AppCompatActivity {

    private TextView scoreDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scores);

        scoreDataText = findViewById(R.id.scoreDataText);
        Button backBtn = findViewById(R.id.backBtn);

        fetchAllScores();

        backBtn.setOnClickListener(v -> finish());
    }

    private void fetchAllScores() {
        String url = "http://10.0.2.2/javastart_api/get_all_scores.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    if (response.length() == 0) {
                        scoreDataText.setText("No quiz records found yet.");
                        return;
                    }

                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String name = obj.getString("fullname");
                            String topic = obj.getString("topic");
                            String score = obj.getString("score");

                            builder.append("STUDENT: ").append(name).append("\n")
                                    .append("QUIZ: ").append(topic).append("\n")
                                    .append("SCORE: ").append(score).append("/5\n")
                                    .append("----------------------------\n");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    scoreDataText.setText(builder.toString());
                },
                error -> {
                    Toast.makeText(this, "Server Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    scoreDataText.setText("Failed to load data.");
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}