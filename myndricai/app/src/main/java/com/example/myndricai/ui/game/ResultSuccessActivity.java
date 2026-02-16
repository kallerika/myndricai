package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.R;
import com.example.myndricai.common.IntentKeys;
import com.example.myndricai.ui.cases.CasesActivity;

public class ResultSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_success);

        String deduction = getIntent().getStringExtra(IntentKeys.EXTRA_USER_DEDUCTION);
        if (deduction == null) deduction = "";

        TextView tvYourDeductionText = findViewById(R.id.tvYourDeductionText);
        if (tvYourDeductionText != null) {
            tvYourDeductionText.setText("«" + deduction.trim() + "»");
        }

        findViewById(R.id.btnExitPhone).setOnClickListener(v -> openMenu(null));
        findViewById(R.id.btnNewCase).setOnClickListener(v -> openMenu("Сейчас все доступные сюжеты пройдены"));
    }

    private void openMenu(String toast) {
        Intent i = new Intent(this, CasesActivity.class);
        if (toast != null) i.putExtra(IntentKeys.EXTRA_TOAST_MESSAGE, toast);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
