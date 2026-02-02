package com.example.javastart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        signUpButton.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String name = nameInput.getText().toString().trim();
        String course = courseInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // 10.0.2.2 is for Emulator. Use your PC IP if using a real phone.
        String url = "http://10.0.2.2/javastart_api/register.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("success")) {
                            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, Login.class));
                            finish();
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Response was: " + response);
                        Toast.makeText(this, "Server error. Check Logcat.", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Connection failed. Check XAMPP.", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("course", course);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}