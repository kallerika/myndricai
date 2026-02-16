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
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCounter.setText(s.length() + " символов");
            }
            @Override public void afterTextChanged(Editable s) {}
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

    private boolean matchesAllKeywords(String answer) {
        if (c == null) return false;

        String csv = c.answerKeywordsCsv;
        if (csv == null) return false;

        String a = answer.toLowerCase(Locale.ROOT);
        String[] keywords = csv.split(",");

        for (String kw : keywords) {
            String k = kw.trim().toLowerCase(Locale.ROOT);
            if (k.isEmpty()) continue;
            if (!a.contains(k)) return false;
        }
        return true;
    }
}
