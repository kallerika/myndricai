package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.MyApp;
import com.example.myndricai.R;
import com.example.myndricai.common.IntentKeys;
import com.example.myndricai.data.entity.CaseEntity;
import com.example.myndricai.repo.CaseRepository;

import java.util.Locale;
import java.util.regex.Pattern;


public class ConclusionActivity extends AppCompatActivity {

    private long caseId;
    private CaseEntity c;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusion);

        caseId = getIntent().getLongExtra(IntentKeys.EXTRA_CASE_ID, -1L);
        if (caseId == -1L) {
            Toast.makeText(this, "Кейс не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        c = MyApp.get().cases().getById(caseId);

        EditText et = findViewById(R.id.etConclusion);
        TextView tvCounter = findViewById(R.id.tvCounter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        TextView tvNeedMore = findViewById(R.id.tvNeedMore);
        tvNeedMore.setClickable(true);
        tvNeedMore.setEnabled(true);
        tvNeedMore.setOnClickListener(v ->
                Toast.makeText(ConclusionActivity.this,
                        "Эта функция платная <З, подумайте ещё",
                        Toast.LENGTH_SHORT).show()
        );


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCounter.setText(s.length() + " символов");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            String ans = et.getText() != null ? et.getText().toString() : "";
            if (TextUtils.isEmpty(ans.trim())) {
                Toast.makeText(this, "Введите ваш ответ", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = matchesAllKeywords(ans);

            // Кейс считается завершенным после сдачи заключения
            MyApp.get().cases().setStatus(caseId, CaseRepository.STATUS_COMPLETED);

            Intent i = new Intent(this, ok ? ResultSuccessActivity.class : ResultFailActivity.class);
            i.putExtra(IntentKeys.EXTRA_CASE_ID, caseId);
            i.putExtra(IntentKeys.EXTRA_USER_DEDUCTION, ans); // <-- ВАЖНО
            startActivity(i);
            finish();

        });
    }

    private String normalizeRu(String s) {
        if (s == null) return "";
        String x = s.toLowerCase(Locale.ROOT)
                .replace('ё', 'е')
                .replaceAll("[^a-zа-я0-9]+", " ")
                .trim()
                .replaceAll("\\s+", " ");
        return x;
    }

    private boolean containsAny(String text, String... variants) {
        for (String v : variants) {
            if (v == null) continue;
            String vv = v.trim();
            if (vv.isEmpty()) continue;
            if (text.contains(vv)) return true;
        }
        return false;
    }

    private boolean containsPhraseLike(String text, String w1, String w2) {
        // проверяет что оба слова есть в тексте, даже если далеко друг от друга
        return text.contains(w1) && text.contains(w2);
    }

    private boolean containsAge19(String text) {
        // 19, 19 лет, 19-летняя и т.п.
        boolean digits = Pattern.compile("\\b19\\b").matcher(text).find()
                || Pattern.compile("\\b19\\s*лет\\b").matcher(text).find()
                || Pattern.compile("\\b19\\s*летн").matcher(text).find();

        // "девятнадцать", "девятнадцати", "девятнадцатилетняя"
        boolean words = text.contains("девятнадцат");

        return digits || words;
    }


    private boolean matchesAllKeywords(String answer) {
        if (c == null) return false;

        String a = normalizeRu(answer);

        // 1) Имя (Даша/Дарья/…)
        boolean okName = containsAny(a,
                "даша",
                "дарья",
                "дашка"
        );

        // 2) Возраст 19 (цифрами и словами)
        boolean okAge = containsAge19(a);

        // 3) Профессия (бариста / кофейня / работает в кофейне и т.п.)
        boolean okJob = containsAny(a,
                "бариста",
                "кофейн",       // кофейня, кофейне, кофейни...
                "кофе",         // кофе (шире, но полезно)
                "готовит кофе"
        ) || containsPhraseLike(a, "работает", "кофе"); // "работает ... кофе/кофейне/кофейня"

        return okName && okAge && okJob;
    }
}
