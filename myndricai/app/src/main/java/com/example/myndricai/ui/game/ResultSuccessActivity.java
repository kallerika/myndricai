package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.R;
import com.example.myndricai.common.IntentKeys;
import com.example.myndricai.ui.cases.CasesActivity;

public class ResultSuccessActivity extends AppCompatActivity {

    private long caseId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_success);

        caseId = getIntent().getLongExtra(IntentKeys.EXTRA_CASE_ID, 1L);

        View btnNew = findViewById(R.id.btnNewCase);      // это AppCompatButton — View подходит
        View btnExit = findViewById(R.id.btnExitPhone);   // это TextView — View подходит

        btnExit.setOnClickListener(v -> openMenu(null));
        btnNew.setOnClickListener(v -> openMenu("Сейчас все доступные сюжеты пройдены"));
    }

    private void openMenu(String toast) {
        Intent i = new Intent(this, CasesActivity.class);
        if (toast != null) i.putExtra(IntentKeys.EXTRA_TOAST_MESSAGE, toast);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
