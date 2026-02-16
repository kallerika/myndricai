package com.example.myndricai.ui.cases;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myndricai.MyApp;
import com.example.myndricai.R;
import com.example.myndricai.common.IntentKeys;
import com.example.myndricai.data.entity.CaseEntity;
import com.example.myndricai.repo.CaseRepository;
import com.example.myndricai.ui.game.LockScreenActivity;
import com.example.myndricai.ui.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class CasesActivity extends AppCompatActivity {

    private CaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases);

        android.widget.Button btnStartNew = findViewById(R.id.btnStartNew);
        ImageButton btnProfile = findViewById(R.id.btnProfile);
        RecyclerView rvCases = findViewById(R.id.rvCases);

        adapter = new CaseAdapter(this::onCaseClick);
        rvCases.setAdapter(adapter);

        btnStartNew.setOnClickListener(v -> startNewFlow());
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        // Optional toast from result screen
        String toast = getIntent() != null ? getIntent().getStringExtra(IntentKeys.EXTRA_TOAST_MESSAGE) : null;
        if (toast != null && !toast.trim().isEmpty()) {
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
            getIntent().removeExtra(IntentKeys.EXTRA_TOAST_MESSAGE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        List<CaseEntity> list = MyApp.get().cases().getAll();
        List<CaseUiModel> ui = new ArrayList<>();
        for (CaseEntity e : list) {
            ui.add(new CaseUiModel(e.id, e.title, e.subtitle, e.status));
        }
        adapter.submit(ui);
    }

    private void startNewFlow() {
        if (MyApp.get().cases().allCompleted()) {
            Toast.makeText(this, "Сейчас все доступные сюжеты пройдены", Toast.LENGTH_SHORT).show();
            return;
        }
        // Start the first non-completed case
        for (CaseEntity e : MyApp.get().cases().getAll()) {
            if (e.status != CaseRepository.STATUS_COMPLETED) {
                openLock(e.id);
                return;
            }
        }
    }

    private void onCaseClick(CaseUiModel item) {
        if (item.status == CaseRepository.STATUS_COMPLETED) {
            Toast.makeText(this, "Этот кейс уже завершен", Toast.LENGTH_SHORT).show();
            return;
        }
        openLock(item.id);
    }

    private void openLock(long caseId) {
        Intent i = new Intent(this, LockScreenActivity.class);
        i.putExtra(IntentKeys.EXTRA_CASE_ID, caseId);
        startActivity(i);
    }
}
