package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.R;
import com.example.myndricai.common.IntentKeys;
import com.example.myndricai.ui.cases.CasesActivity;

public class ResultFailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_fail);

        String userText = getIntent().getStringExtra(IntentKeys.EXTRA_USER_DEDUCTION);

        TextView tvUser = findViewById(R.id.tvYourDeductionText);
        if (tvUser != null && userText != null && !userText.trim().isEmpty()) {
            tvUser.setText("«" + userText.trim() + "»");
        }

        findViewById(R.id.btnExitPhone).setOnClickListener(v -> openMenu(null));
        findViewById(R.id.btnNewCase).setOnClickListener(v -> openMenu("Сейчас все доступные сюжеты пройдены"));
    }

    private void openMenu(String toast) {
        Intent i = new Intent(this, com.example.myndricai.ui.cases.CasesActivity.class);
        if (toast != null) i.putExtra(IntentKeys.EXTRA_TOAST_MESSAGE, toast);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
