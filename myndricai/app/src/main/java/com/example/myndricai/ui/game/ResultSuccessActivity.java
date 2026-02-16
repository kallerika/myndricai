package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.R;
import com.example.myndricai.common.constants.IntentKeys;
import com.example.myndricai.di.ServiceLocator;
import com.example.myndricai.domain.model.CaseDefinition;
import com.example.myndricai.domain.model.CaseProgress;
import com.example.myndricai.ui.cases.CasesActivity;
import com.google.android.gms.tasks.Tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSuccessActivity extends AppCompatActivity {

    private String caseId;
    private String userAnswer;

    private TextView tvYourDeductionText;
    private android.view.View btnNewCase;
    private TextView btnExitPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_success);

        caseId = getIntent() != null ? getIntent().getStringExtra(IntentKeys.EXTRA_CASE_ID) : null;
        userAnswer = getIntent() != null ? getIntent().getStringExtra(IntentKeys.EXTRA_USER_ANSWER) : "";

        tvYourDeductionText = findViewById(R.id.tvYourDeductionText);
        btnNewCase = findViewById(R.id.btnNewCase);
        btnExitPhone = findViewById(R.id.btnExitPhone);

        tvYourDeductionText.setText(userAnswer != null ? userAnswer : "");

        btnExitPhone.setOnClickListener(v -> goToMenu(null));
        btnNewCase.setOnClickListener(v -> startNextCaseOrMenu());
    }

    private void startNextCaseOrMenu() {
        Tasks.whenAllSuccess(
                ServiceLocator.get().caseContentRepository().loadCasesWithFallback(),
                ServiceLocator.get().casesRepository().loadProgress()
        ).addOnSuccessListener(results -> {
            @SuppressWarnings("unchecked")
            List<CaseDefinition> defs = (List<CaseDefinition>) results.get(0);
            @SuppressWarnings("unchecked")
            List<CaseProgress> progress = (List<CaseProgress>) results.get(1);

            String nextId = findNextNotStarted(defs, progress);
            if (nextId == null) {
                goToMenu("Сейчас все доступные сюжеты пройдены");
                return;
            }

            ServiceLocator.get().casesRepository().markInProgress(nextId)
                    .addOnCompleteListener(t -> {
                        Intent i = new Intent(this, LockScreenActivity.class);
                        i.putExtra(IntentKeys.EXTRA_CASE_ID, nextId);
                        startActivity(i);
                        finish();
                    });
        }).addOnFailureListener(e -> goToMenu("Сейчас все доступные сюжеты пройдены"));
    }

    private String findNextNotStarted(List<CaseDefinition> defs, List<CaseProgress> progress) {
        Map<String, String> status = new HashMap<>();
        if (progress != null) {
            for (CaseProgress p : progress) {
                if (p != null && p.caseId != null) status.put(p.caseId, p.status);
            }
        }
        if (defs == null) return null;

        for (CaseDefinition d : defs) {
            if (d == null || d.caseId == null) continue;
            String st = status.get(d.caseId);
            if (st == null || CaseProgress.STATUS_NOT_STARTED.equals(st)) {
                return d.caseId;
            }
        }
        return null;
    }

    private void goToMenu(@Nullable String toast) {
        Intent i = new Intent(this, CasesActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (toast != null) i.putExtra(IntentKeys.EXTRA_TOAST_MESSAGE, toast);
        startActivity(i);
        finish();
    }
}
