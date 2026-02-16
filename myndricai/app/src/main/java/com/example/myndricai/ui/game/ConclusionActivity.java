package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.R;
import com.example.myndricai.common.constants.IntentKeys;
import com.example.myndricai.common.util.KeywordMatcher;
import com.example.myndricai.di.ServiceLocator;
import com.example.myndricai.domain.model.CaseDefinition;

import java.util.List;

public class ConclusionActivity extends AppCompatActivity {

    private String caseId;

    private ImageButton btnBack;
    private EditText etConclusion;
    private TextView tvCounter;
    private TextView tvNeedMore;
    private android.view.View btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusion);

        caseId = getIntent() != null ? getIntent().getStringExtra(IntentKeys.EXTRA_CASE_ID) : null;

        btnBack = findViewById(R.id.btnBack);
        etConclusion = findViewById(R.id.etConclusion);
        tvCounter = findViewById(R.id.tvCounter);
        tvNeedMore = findViewById(R.id.tvNeedMore);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnBack.setOnClickListener(v -> finish());

        tvNeedMore.setOnClickListener(v ->
                Toast.makeText(this, "Эта функция платная <З, подумайте ещё", Toast.LENGTH_SHORT).show()
        );

        etConclusion.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s != null ? s.length() : 0;
                tvCounter.setText(len + " символов");
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnSubmit.setOnClickListener(v -> submit());
    }

    private void submit() {
        String answer = etConclusion.getText() != null ? etConclusion.getText().toString().trim() : "";
        if (answer.isEmpty()) {
            Toast.makeText(this, "Введите вашу версию", Toast.LENGTH_SHORT).show();
            return;
        }

        // Берём контент кейса (Firestore -> fallback local), находим keywords, проверяем
        ServiceLocator.get().caseContentRepository().loadCasesWithFallback()
                .addOnSuccessListener(defs -> {
                    CaseDefinition def = findById(defs, caseId);
                    List<String> keywords = def != null ? def.requiredKeywords : null;

                    boolean ok = KeywordMatcher.isFullMatch(answer, keywords);

                    // помечаем кейс завершённым независимо от результата (как в твоём сценарии: статус "завершен" после прохождения)
                    ServiceLocator.get().casesRepository().markCompleted(caseId)
                            .addOnCompleteListener(t -> openResult(ok, answer));
                })
                .addOnFailureListener(e -> {
                    // если не смогли загрузить контент — всё равно дадим пройти, но почти наверняка будет fail
                    ServiceLocator.get().casesRepository().markCompleted(caseId)
                            .addOnCompleteListener(t -> openResult(false, answer));
                });
    }

    private void openResult(boolean success, String answer) {
        Intent i = new Intent(this, success ? ResultSuccessActivity.class : ResultFailActivity.class);
        i.putExtra(IntentKeys.EXTRA_CASE_ID, caseId);
        i.putExtra(IntentKeys.EXTRA_USER_ANSWER, answer);
        startActivity(i);
        finish();
    }

    private CaseDefinition findById(List<CaseDefinition> defs, String caseId) {
        if (defs == null || caseId == null) return null;
        for (CaseDefinition d : defs) {
            if (d != null && caseId.equals(d.caseId)) return d;
        }
        return null;
    }
}
