package com.example.myndricai.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.MyApp;
import com.example.myndricai.R;
import com.example.myndricai.ui.auth.LoginActivity;
import com.example.myndricai.ui.cases.CasesActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnStartQuest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartQuest = findViewById(R.id.btnStartQuest);
        if (btnStartQuest != null) {
            btnStartQuest.setOnClickListener(v -> openNext());
        }
    }

    private void openNext() {
        Intent i;
        if (MyApp.get().session().isLoggedIn()) {
            i = new Intent(this, CasesActivity.class);
        } else {
            i = new Intent(this, LoginActivity.class);
        }
        startActivity(i);
    }
}
