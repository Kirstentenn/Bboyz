package com.example.javastart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    // Added surnameInput here
    private EditText nameInput, surnameInput, courseInput, emailInput, passwordInput;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Mapping the new ID you created
        nameInput = findViewById(R.id.Name);
        surnameInput = findViewById(R.id.surname); // Make sure this ID matches your XML exactly
        courseInput = findViewById(R.id.Course);
        emailInput = findViewById(R.id.email1);
        passwordInput = findViewById(R.id.pw2);
        signUpButton = findViewById(R.id.button);

        signUpButton.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String name = nameInput.getText().toString().trim();
        String surname = surnameInput.getText().toString().trim(); // Capture surname
        String course = courseInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Updated validation to include surname
        if (name.isEmpty() || surname.isEmpty() || course.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.toLowerCase().endsWith("@gmail.com")) {
            emailInput.setError("Only @gmail.com addresses are allowed");
            emailInput.requestFocus();
            return;
        }

        String url = "http://10.0.2.2/javastart_api/register.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            Toast.makeText(SignUp.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Response: " + response);
                        Toast.makeText(this, "Server formatting error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VOLLEY_ERROR", error.toString());
                    Toast.makeText(this, "Connection failed. Check XAMPP/IP.", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // These keys MUST match the $_POST['key'] in your PHP script
                params.put("firstname", name);
                params.put("surname", surname);
                params.put("course", course);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(request);
    }
}