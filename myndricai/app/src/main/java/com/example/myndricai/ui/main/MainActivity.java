package com.example.myndricai.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.R;
import com.example.myndricai.di.ServiceLocator;
import com.example.myndricai.ui.auth.LoginActivity;
import com.example.myndricai.ui.cases.CasesActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Если уже авторизован — сразу в меню кейсов
        if (ServiceLocator.get().authRepository().isSignedIn()) {
            startActivity(new Intent(this, CasesActivity.class));
            finish();
            return;
        }

        // Единственная кнопка на экране: "Начать квест"
        findViewById(R.id.btnStartQuest).setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );
    }
}
