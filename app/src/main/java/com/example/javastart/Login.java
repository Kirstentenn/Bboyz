package com.example.javastart;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Login extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton, signupBtn; // Added signupBtn to make that button work too

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // MATCHING YOUR XML IDs: email, password, login, signup
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signupBtn = findViewById(R.id.signup);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                performLogin(email, password);
            }
        });

        // Makes the "Sign Up" button actually take you to the Sign Up screen
        signupBtn.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, SignUp.class));
        });
    }

    private void performLogin(String email, String password) {
        String url = "http://10.0.2.2/javastart_api/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (status.equals("success")) {
                            SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("email", email);
                            editor.putString("name", jsonObject.getString("name"));
                            editor.putString("role", jsonObject.getString("role"));

                            // Save course from the DB so Profile activity works
                            if (jsonObject.has("course")) {
                                editor.putString("course", jsonObject.getString("course"));
                            }

                            editor.apply();

                            String role = jsonObject.getString("role");
                            if (role.equalsIgnoreCase("admin")) {
                                startActivity(new Intent(Login.this, AdminMenu.class));
                            } else {
                                startActivity(new Intent(Login.this, Menu.class));
                            }
                            finish();

                        } else {
                            Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Login.this, "Server response error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(Login.this, "Network Error: Check XAMPP", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}