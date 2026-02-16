package com.example.myndricai.ui.cases;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myndricai.R;
import com.example.myndricai.common.constants.IntentKeys;
import com.example.myndricai.common.result.UiState;
import com.example.myndricai.di.ServiceLocator;
import com.example.myndricai.ui.game.LockScreenActivity;
import com.example.myndricai.ui.profile.ProfileActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CasesActivity extends AppCompatActivity {

    private CasesViewModel vm;

    private MaterialButton btnStartNew;
    private ImageButton btnProfile;
    private RecyclerView rvCases;

    private CaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases);

        btnStartNew = findViewById(R.id.btnStartNew);
        btnProfile = findViewById(R.id.btnProfile);
        rvCases = findViewById(R.id.rvCases);

        adapter = new CaseAdapter(item -> vm.onCaseClick(item));
        rvCases.setAdapter(adapter);

        vm = new ViewModelProvider(this, new VmFactory()).get(CasesViewModel.class);

        btnStartNew.setOnClickListener(v -> vm.onStartNewClick());
        btnProfile.setOnClickListener(v -> vm.onProfileClick());

        vm.getCasesState().observe(this, state -> {
            if (state instanceof UiState.Loading) {
                btnStartNew.setEnabled(false);
            } else if (state instanceof UiState.Content) {
                btnStartNew.setEnabled(true);
                @SuppressWarnings("unchecked")
                List<CaseUiModel> list = ((UiState.Content<List<CaseUiModel>>) state).data;
                adapter.submitList(list);
            } else if (state instanceof UiState.Error) {
                btnStartNew.setEnabled(true);
                String msg = ((UiState.Error<?>) state).message;
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            } else {
                btnStartNew.setEnabled(true);
            }
        });

        vm.getToastEvent().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        );

        vm.getNavEvent().observe(this, nav -> {
            if (nav == null) return;

            if ("profile".equals(nav.destination)) {
                startActivity(new Intent(this, ProfileActivity.class));
                return;
            }

            if ("lock".equals(nav.destination)) {
                Intent i = new Intent(this, LockScreenActivity.class);
                i.putExtra(IntentKeys.EXTRA_CASE_ID, nav.caseId);
                startActivity(i);
            }
        });

        // Показать уведомление, если пришли с result-экрана
        String toast = getIntent() != null ? getIntent().getStringExtra(IntentKeys.EXTRA_TOAST_MESSAGE) : null;
        if (toast != null && !toast.trim().isEmpty()) {
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
            // Чтобы не показывать повторно при повороте/возврате
            getIntent().removeExtra(IntentKeys.EXTRA_TOAST_MESSAGE);
        }

        vm.load();
    }

    private static class VmFactory implements ViewModelProvider.Factory {
        @SuppressWarnings("unchecked")
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CasesViewModel.class)) {
                return (T) new CasesViewModel(
                        ServiceLocator.get().caseContentRepository(),
                        ServiceLocator.get().casesRepository()
                );
            }
            throw new IllegalArgumentException("Unknown ViewModel: " + modelClass);
        }
    }
}
