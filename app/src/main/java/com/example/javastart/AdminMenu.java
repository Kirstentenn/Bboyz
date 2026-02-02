package com.example.javastart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminMenu extends AppCompatActivity {

    private Button btnViewScores, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        btnViewScores = findViewById(R.id.btnViewScores);
        btnLogout = findViewById(R.id.btnAdminLogout);

        // This will stop being red once ViewScores.java is recognized in the package
        btnViewScores.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMenu.this, ViewScores.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
            sp.edit().clear().apply();

            Intent intent = new Intent(AdminMenu.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}