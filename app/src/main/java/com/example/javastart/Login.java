package com.example.javastart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton, signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- ADDED: Auto-login check ---
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        if (sp.contains("email")) {
            startActivity(new Intent(Login.this, Menu.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signupBtn = findViewById(R.id.signup);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                performLogin(email, password);
            }
        });

        signupBtn.setOnClickListener(v -> startActivity(new Intent(Login.this, SignUp.class)));
    }

    private void performLogin(String email, String password) {
        String url = "http://10.0.2.2/javastart_api/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            // Store user data in SharedPreferences
                            SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("email", email);
                            editor.putString("name", jsonObject.getString("name"));
                            editor.apply();

                            Toast.makeText(Login.this, "Welcome, " + jsonObject.getString("name"), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(Login.this, Menu.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Login.this, "Json Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(Login.this, "Connection Error: Check Server", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}