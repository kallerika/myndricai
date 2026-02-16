package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.R;
import com.example.myndricai.common.constants.IntentKeys;
import com.example.myndricai.ui.cases.CasesActivity;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhoneDesktopActivity extends AppCompatActivity {

    private String caseId;

    private TextView tvTopTime;

    private MaterialButton btnConclusion;
    private MaterialButton btnExitPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_desktop);

        caseId = getIntent() != null ? getIntent().getStringExtra(IntentKeys.EXTRA_CASE_ID) : null;

        tvTopTime = findViewById(R.id.tvTopTime);
        btnConclusion = findViewById(R.id.btnConclusion);
        btnExitPhone = findViewById(R.id.btnExitPhone);

        updateTime();

        // Выйти из игры -> вернуть в меню кейсов
        btnExitPhone.setOnClickListener(v -> goToCases());

        // Заключение
        btnConclusion.setOnClickListener(v -> {
            Intent i = new Intent(this, ConclusionActivity.class);
            i.putExtra(IntentKeys.EXTRA_CASE_ID, caseId);
            startActivity(i);
        });

        // Открываем нужные экраны
        findViewById(R.id.appGallery).setOnClickListener(v -> open(GalleryActivity.class));
        findViewById(R.id.appCalendar).setOnClickListener(v -> open(CalendarActivity.class));
        findViewById(R.id.appMessages).setOnClickListener(v -> open(MessagesActivity.class));

        // Dock
        findViewById(R.id.btnDockGallery).setOnClickListener(v -> open(GalleryActivity.class));
        findViewById(R.id.btnDockMessages).setOnClickListener(v -> open(MessagesActivity.class));
        findViewById(R.id.btnDockPhone).setOnClickListener(v -> toastUnavailable());

        // Остальные иконки - заглушки
        findViewById(R.id.appEmail).setOnClickListener(v -> toastUnavailable());
        findViewById(R.id.appNotes).setOnClickListener(v -> toastUnavailable());
        findViewById(R.id.appSettings).setOnClickListener(v -> toastUnavailable());
        findViewById(R.id.appCamera).setOnClickListener(v -> toastUnavailable());
        findViewById(R.id.appPhonebook).setOnClickListener(v -> toastUnavailable());
    }

    private void updateTime() {
        tvTopTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
    }

    private void open(Class<?> cls) {
        Intent i = new Intent(this, cls);
        i.putExtra(IntentKeys.EXTRA_CASE_ID, caseId);
        startActivity(i);
    }

    private void toastUnavailable() {
        Toast.makeText(this, "Эта функция недоступна", Toast.LENGTH_SHORT).show();
    }

    private void goToCases() {
        Intent i = new Intent(this, CasesActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}
