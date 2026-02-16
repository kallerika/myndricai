package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.MyApp;
import com.example.myndricai.R;
import com.example.myndricai.common.IntentKeys;
import com.example.myndricai.data.entity.CaseEntity;
import com.example.myndricai.repo.CaseRepository;

public class LockScreenActivity extends AppCompatActivity {

    private long caseId;
    private CaseEntity c;

    private final StringBuilder pin = new StringBuilder();
    private View dot1, dot2, dot3, dot4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        caseId = getIntent().getLongExtra(IntentKeys.EXTRA_CASE_ID, 1L);
        c = MyApp.get().cases().getById(caseId);

        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);

        bindDigit(R.id.btn1, "1");
        bindDigit(R.id.btn2, "2");
        bindDigit(R.id.btn3, "3");
        bindDigit(R.id.btn4, "4");
        bindDigit(R.id.btn5, "5");
        bindDigit(R.id.btn6, "6");
        bindDigit(R.id.btn7, "7");
        bindDigit(R.id.btn8, "8");
        bindDigit(R.id.btn9, "9");
        bindDigit(R.id.btn0, "0");

        TextView btnBackspace = findViewById(R.id.btnBackspace);
        btnBackspace.setOnClickListener(v -> backspace());

        TextView btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> checkPin());

        updateDots();
    }

    private void bindDigit(int viewId, String digit) {
        TextView tv = findViewById(viewId);
        tv.setOnClickListener(v -> {
            if (pin.length() >= 4) return;
            pin.append(digit);
            updateDots();
            if (pin.length() == 4) checkPin();
        });
    }

    private void backspace() {
        if (pin.length() == 0) return;
        pin.deleteCharAt(pin.length() - 1);
        updateDots();
    }

    private void updateDots() {
        int n = pin.length();
        setDot(dot1, n >= 1);
        setDot(dot2, n >= 2);
        setDot(dot3, n >= 3);
        setDot(dot4, n >= 4);
    }

    private void setDot(View v, boolean filled) {
        if (v == null) return;
        v.setAlpha(filled ? 1f : 0.25f);
    }

    private void checkPin() {
        if (c == null) {
            Toast.makeText(this, "Кейс не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (pin.length() != 4) {
            Toast.makeText(this, "Введите 4 цифры", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pin.toString().equals(c.pin)) {
            MyApp.get().cases().setStatus(caseId, CaseRepository.STATUS_IN_PROGRESS);
            Intent i = new Intent(this, PhoneDesktopActivity.class);
            i.putExtra(IntentKeys.EXTRA_CASE_ID, caseId);
            startActivity(i);
            finish();
        } else {
            pin.setLength(0);
            updateDots();
            Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show();
        }
    }
}
