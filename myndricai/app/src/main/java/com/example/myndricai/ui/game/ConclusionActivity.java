package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
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

        caseId = getIntent().getLongExtra(IntentKeys.EXTRA_CASE_ID, 1L);
        c = MyApp.get().cases().getById(caseId);

        EditText et = findViewById(R.id.etConclusion);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnNeedMore = findViewById(R.id.tvNeedMore);

        btnNeedMore.setOnClickListener(v ->
                Toast.makeText(this, "Эта функция платная <З, подумайте ещё", Toast.LENGTH_SHORT).show()
        );

        btnSubmit.setOnClickListener(v -> {
            String ans = et.getText() != null ? et.getText().toString() : "";
            if (TextUtils.isEmpty(ans.trim())) {
                Toast.makeText(this, "Введите ваш ответ", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean ok = matchesAllKeywords(ans);

            // Case считается завершенным после сдачи заключения.
            MyApp.get().cases().setStatus(caseId, CaseRepository.STATUS_COMPLETED);

            Intent i = new Intent(this, ok ? ResultSuccessActivity.class : ResultFailActivity.class);
            i.putExtra(IntentKeys.EXTRA_CASE_ID, caseId);
            startActivity(i);
            finish();
        });
    }

    private boolean matchesAllKeywords(String answer) {
        if (c == null) return false;
        String a = answer.toLowerCase(Locale.ROOT);
        String[] keywords = c.answerKeywordsCsv.split(",");
        for (String kw : keywords) {
            String k = kw.trim().toLowerCase(Locale.ROOT);
            if (k.isEmpty()) continue;
            if (!a.contains(k)) return false;
        }
        return true;
    }
}
