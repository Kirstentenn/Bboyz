package com.example.javastart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.content.Intent;

public class SignUp extends AppCompatActivity {

    private EditText nameInput, courseInput, emailInput, passwordInput;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameInput = findViewById(R.id.Name);
        courseInput = findViewById(R.id.Course);
        emailInput = findViewById(R.id.email1);
        passwordInput = findViewById(R.id.pw2);
        signUpButton = findViewById(R.id.button);

        signUpButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String course = courseInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (name.isEmpty() || course.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUp.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidNameOrCourse(name) || !isValidNameOrCourse(course)) {
                Toast.makeText(SignUp.this, "Invalid name or course! Only letters and spaces are allowed.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(SignUp.this, "Invalid email! Use @gmail.com, @yahoo.com, or @students.nu-dasma.edu.ph", Toast.LENGTH_LONG).show();
                return;
            }

            // API URL
            String url = "http://10.0.2.2/javastart_api/register.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("success")){
                                Toast.makeText(SignUp.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                // Go back to login
                                Intent intent = new Intent(SignUp.this, Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignUp.this, "Error: " + obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignUp.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(SignUp.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("course", course);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(SignUp.this);
            queue.add(stringRequest);
        });
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@((gmail\\.com)|(yahoo\\.com)|(students\\.nu-dasma\\.edu\\.ph))$");
    }

    private boolean isValidNameOrCourse(String input) {
        return input.matches("^[A-Za-z-. ]+$");
    }
}
