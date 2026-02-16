package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.R;
import com.example.myndricai.common.constants.IntentKeys;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LockScreenActivity extends AppCompatActivity {

    // Простой стабильный PIN (можно позже сделать разным для кейсов)
    private static final String CORRECT_PIN = "1234";

    private String caseId;

    private TextView tvBigTime;
    private TextView tvDate;

    private View dot1, dot2, dot3, dot4;

    private StringBuilder entered = new StringBuilder(4);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        caseId = getIntent() != null ? getIntent().getStringExtra(IntentKeys.EXTRA_CASE_ID) : null;

        tvBigTime = findViewById(R.id.tvBigTime);
        tvDate = findViewById(R.id.tvDate);

        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);

        bindKeypad();

        updateTime();
    }

    private void updateTime() {
        Date now = new Date();
        tvBigTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now));
        tvDate.setText(new SimpleDateFormat("EEEE, d MMMM", new Locale("ru")).format(now));
    }

    private void bindKeypad() {
        bindDigit(R.id.btn0, "0");
        bindDigit(R.id.btn1, "1");
        bindDigit(R.id.btn2, "2");
        bindDigit(R.id.btn3, "3");
        bindDigit(R.id.btn4, "4");
        bindDigit(R.id.btn5, "5");
        bindDigit(R.id.btn6, "6");
        bindDigit(R.id.btn7, "7");
        bindDigit(R.id.btn8, "8");
        bindDigit(R.id.btn9, "9");

        MaterialButton btnBackspace = findViewById(R.id.btnBackspace);
        btnBackspace.setOnClickListener(v -> {
            if (entered.length() > 0) {
                entered.deleteCharAt(entered.length() - 1);
                updateDots();
            }
        });
    }

    private void bindDigit(int btnId, String digit) {
        MaterialButton b = findViewById(btnId);
        b.setOnClickListener(v -> {
            if (entered.length() >= 4) return;
            entered.append(digit);
            updateDots();

            if (entered.length() == 4) {
                checkPin();
            }
        });
    }

    private void updateDots() {
        setDot(dot1, entered.length() >= 1);
        setDot(dot2, entered.length() >= 2);
        setDot(dot3, entered.length() >= 3);
        setDot(dot4, entered.length() >= 4);
    }

    private void setDot(View dot, boolean filled) {
        dot.setAlpha(filled ? 1.0f : 0.35f);
    }

    private void checkPin() {
        if (CORRECT_PIN.contentEquals(entered)) {
            Intent i = new Intent(this, PhoneDesktopActivity.class);
            i.putExtra(IntentKeys.EXTRA_CASE_ID, caseId);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, "Неверный код", Toast.LENGTH_SHORT).show();
            entered.setLength(0);
            updateDots();
        }
    }
}
