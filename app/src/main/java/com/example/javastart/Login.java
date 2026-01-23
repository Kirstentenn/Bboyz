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
    private Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        login.setOnClickListener(v -> {
            String enteredEmail = emailInput.getText().toString().trim();
            String enteredPassword = passwordInput.getText().toString().trim();

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // URL of your PHP login API
            String url = "http://10.0.2.2/javastart_api/login.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                String name = jsonObject.getString("name");
                                String role = jsonObject.getString("role");

                                Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                // Save session
                                SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("name", name);
                                editor.putString("email", enteredEmail);
                                editor.putString("role", role);
                                editor.apply();

                                // Route user/admin
                                Intent intent;
                                if (role.equals("admin")) {
                                    intent = new Intent(Login.this, AdminMenu.class);
                                } else {
                                    intent = new Intent(Login.this, Menu.class);
                                }

                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(Login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", enteredEmail);
                    params.put("password", enteredPassword);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(Login.this);
            queue.add(stringRequest);
        });

        signup.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });
    }
}
